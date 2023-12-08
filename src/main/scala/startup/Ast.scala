package startup.ast

/** Package `startup.ast` contains classes and objects that represent the
  * abstract syntax tree (AST) for the startup application.
  *
  * The AST is a hierarchical tree-like data structure that represents the
  * structure of the source code. Each node in the tree denotes a construct
  * occurring in the source code.
  *
  * The AST in this package is capable of handling various types of expressions,
  * including numeric values and arithmetic operations such as addition and
  * multiplication. It provides methods to evaluate these expressions, using the
  * operations defined by an implicit `ArithmeticOperation[T]` instance for the
  * type `T` of the values in the expression.
  *
  * The package also includes functionality for serializing and deserializing
  * the AST to and from JSON. This is done using the `upickle.default._`
  * library, which provides `ReadWriter` instances for reading and writing Scala
  * objects as JSON. These `ReadWriter` instances are implicitly available for
  * any type `T` that has a `ReadWriter[T]` instance.
  *
  * A special note on DataFrame serialization: The `DataFrameName` class is used
  * to represent the name of a DataFrame, which is a path to a Parquet file.
  * When a DataFrame is serialized, only the DataFrame name is written as JSON,
  * not the full content of the DataFrame. This is because DataFrames can be
  * very large, and it's more efficient to read and write them directly from and
  * to the file system. The `DataFrameName` class provides extension methods for
  * `Dataset[Row]` and `DataFrameName` to write a dataset as a Parquet file and
  * read a Parquet file as a dataset, respectively.
  *
  * This package is part of the `startup` application.
  */

import app.SparkMain.{sparkSession}
import java.nio.file.{Paths, Files}

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, concat}

/*
/** A dummy class to test project setup */
case class myStart(param: Int):

  /** A dummy value */
  val startvalue: String = "test"

  /** @param two integers to add */
  /** @return the addition of the 2 inputs */
  def add(x: Int, y: Int): Int = x + y

object myStart:
  given rw: RW[myStart] = macroRW

case class Arg(arg: myStart)
object Arg:
  given argRW: RW[Arg] = macroRW
*/

/** A type class that defines arithmetic operations for a type `T`.
  */
trait ArithmeticOperation[T]:
  /** Adds two values of type `T`.
    */
  def add(x: T, y: T): T

  /** Multiplies two values of type `T`.
    */
  def mul(x: T, y: T): T

/** Companion object for `ArithmeticOperation` that provides given instances for
  * `Int` and `Double`.
  */
object ArithmeticOperation:

  /** Given instance of `ArithmeticOperation` for `Int`.
    */
  given IntOps: ArithmeticOperation[Int] with
    def add(x: Int, y: Int): Int = x + y
    def mul(x: Int, y: Int): Int = x * y

  /** Given instance of `ArithmeticOperation` for `Double`.
    */
  given DoubleOps: ArithmeticOperation[Double] with
    def add(x: Double, y: Double): Double = x + y
    def mul(x: Double, y: Double): Double = x * y

  /** Given instance of `ArithmeticOperation` for `DataFrame`.
    */
  given DfOps: ArithmeticOperation[Dataset[Row]] with
    def add(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      //dfx.show()
      //dfy.show()
      val resultDf =
        dfx
          .alias("dfx")
          .join(dfy.alias("dfy"), "index")
          .select(
            (col("dfx.index")).as("index"),
            (col("dfx.doubleField") + col("dfy.doubleField")).as("doubleField"),
            concat(col("dfx.stringField"), col("dfy.stringField"))
              .as("stringField"),
            (col("dfx.intField") + col("dfy.intField")).as("intField")
          )
      //resultDf.show()
      resultDf

    def mul(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      //dfx.show()
      //dfy.show()
      //println("ready")
      val resultDf =
        dfx
          .alias("dfx")
          .join(dfy.alias("dfy"), "index")
          .select(
            (col("dfx.index")).as("index"),
            (col("dfx.doubleField") * col("dfy.doubleField")).as("doubleField"),
            concat(col("dfx.stringField"),col("dfx.stringField"),col("dfy.stringField"),col("dfy.stringField"))
              .as("stringField"),
            (col("dfx.intField") * col("dfy.intField")).as("intField")
          )
      //resultDf.show()
      //println("see results")
      resultDf

/** A sealed trait representing an arithmetic expression.
  */
sealed trait Expression[T]

/** A case class representing a number in an arithmetic expression.
  */
case class Num[T](n: T) extends Expression[T]

/** A case class representing a multiplication operation in an arithmetic
  * expression.
  */
case class Mult[T](a: Expression[T], b: Expression[T]) extends Expression[T]

/** A case class representing an addition operation in an arithmetic expression.
  */
case class Plus[T](a: Expression[T], b: Expression[T]) extends Expression[T]

/** Companion object for `Expression` that provides a method to evaluate an
  * expression.
  */
object Expression:
  /** Evaluates an arithmetic expression.
    *
    * @param expression
    *   The expression to evaluate.
    * @param ops
    *   The given instance of `ArithmeticOperation` for the type `T`.
    * @return
    *   The result of the evaluation.
    */
  def evaluate[T](expression: Expression[T])(using
      ops: ArithmeticOperation[T]
  ): T =
    expression match
      case Num[T](num)                      => num
      case Mult[T](a, b)                    => ops.mul(evaluate(a), evaluate(b))
      case Plus[T](a, b)                    => ops.add(evaluate(a), evaluate(b))
  end evaluate

  /** Provides an implicit `ReadWriter[Expression[T]]` instance for any type `T`
    * that has a `ReadWriter[T]` instance.
    *
    * This instance can read and write `Expression[T]` instances as JSON. It's
    * created by merging the `ReadWriter` instances for `Num[T]`, `Plus[T]`, and
    * `Mult[T]`.
    *
    * @param RW[T]
    *   The given `ReadWriter[T]` instance for the type `T`.
    * @return
    *   The `ReadWriter[Expression[T]]` instance.
    */
  given [T](using RW[T]): RW[Expression[T]] = RW.merge(
    macroRW[Num[T]],
    macroRW[Plus[T]],
    macroRW[Mult[T]]
  )

  def validateExpression[T](expression: Expression[T]): Either[String, Expression[T]] =
    expression match
    case Num(n) => Right(expression) 
    case Mult(a, b) => (validateExpression(a), validateExpression(b)) match
        case (Right(_), Right(_)) => Right(expression)
        case _ => Left("Invalid multiplication expression")
    case Plus(a, b) => (validateExpression(a), validateExpression(b)) match
        case (Right(_), Right(_)) => Right(expression)
        case _ => Left("Invalid addition expression")

end Expression

/** A case class that wraps an `Expression[T]` instance.
  *
  * This class is used to serialize `Expression[T]` instances.
  *
  * @param argast
  *   The `Expression[T]` instance to serialize.
  * @tparam T
  *   The type of the values in the expression.
  */
case class ExpressionToSerialize[T](argast: Either[String, Expression[T]])//Expression[T])

/** Companion object for `ExpressionToSerialize` that provides an implicit
  * `ReadWriter` instance.
  */
object ExpressionToSerialize:
  /** Provides an implicit `ReadWriter[ExpressionToSerialize[T]]` instance for
    * any type `T` that has a `ReadWriter[T]` instance.
    *
    * This instance can read and write `ExpressionToSerialize[T]` instances as
    * JSON.
    *
    * @param RW[T]
    *   The given `ReadWriter[T]` instance for the type `T`.
    * @return
    *   The `ReadWriter[ExpressionToSerialize[T]]` instance.
    */
  given [T](using RW[T]): RW[ExpressionToSerialize[T]] =
    macroRW[ExpressionToSerialize[T]]

  // Additional given instance for Either[String, ExpressionToSerialize[T]]
  /*
  given [T](using rwT: RW[T])(using rwExT: RW[ExpressionToSerialize[T]]): RW[Either[String, ExpressionToSerialize[T]]] =
    RW.merge(
      //summon[rwT]
      rwT.bimap[Either[String, ExpressionToSerialize[T]]]({
        case Left(x) => write(x)
        case Right(_) => throw new IllegalStateException("Attempted to serialize Right as Left")},
        Left(_)),
      //summon[rwExT]
      rwExt.bimap[Either[String, ExpressionToSerialize[T]]]({
        case Right(exp) => exp
        case Left(_) => throw new IllegalStateException("Attempted to serialize Left as Right")},
        Right(_))
    )
  */
  given [T](using rws: RW[String], rwt: RW[T])(using rwExT: RW[ExpressionToSerialize[T]]): RW[Either[String, ExpressionToSerialize[T]]] =
    RW.merge(
      rws.bimap[Either[String, ExpressionToSerialize[T]]](
      {
        case Left(str) => str
        case Right(_) => throw new Exception("Not a Left value")
      },
      str => Left(str)
      ),
      rwExT.bimap[Either[String, ExpressionToSerialize[T]]](
        {
          case Right(expr) => expr
          case Left(_) => throw new Exception("Not a Right value")
        },
        expr => Right(expr)
      )
    )

/** A case class that represents the name of a DataFrame.
  *
  * @param name
  *   The name of the DataFrame, which is a path to a Parquet file.
  */
case class DataFrameName(name: String)

/** Companion object for `DataFrameName` that provides several extension methods
  * and implicit instances.
  */
object DataFrameName:

  /** Provides an extension method for `Dataset[Row]` to write the dataset as a
    * Parquet file.
    *
    * The method generates a path based on the hash code of the dataset, writes
    * the dataset to this path as a Parquet file, and returns a `DataFrameName`
    * instance that represents the path.
    *
    * @param df
    *   The dataset to write.
    * @return
    *   The `DataFrameName` instance that represents the path to the Parquet
    *   file.
    */
  extension (df: Dataset[Row])

    def toDataFrameName: DataFrameName =
      val dfId = df.contentHash
      val name = DataFrameName(dfId)
      name.writeAsParquet(df)
      name
    
    def contentHash: String =
      val firstColumnName = df.columns(0)
      val sortedDf = df.sort(firstColumnName)
      val contentString = sortedDf.collect().mkString
      val md = java.security.MessageDigest.getInstance("SHA-256")
      val hash = md.digest(contentString.getBytes("UTF-8")).map("%02x".format(_)).mkString
      hash

  extension (name: DataFrameName)

    def writeAsParquet(df: Dataset[Row]): Unit =
      val path = s"./dataframes/${name.name}.parquet"
      if (!Files.exists(Paths.get(path))) then
        df.coalesce(1).write.mode("overwrite").parquet(path)

  /** Provides an extension method for `DataFrameName` to read the Parquet file
    * as a `Dataset[Row]`.
    *
    * The method uses the name of the `DataFrameName` instance as the path to
    * the Parquet file.
    *
    * @param name
    *   The `DataFrameName` instance that represents the path to the Parquet
    *   file.
    * @return
    *   The dataset read from the Parquet file.
    */
    def readAsDataFrame: Dataset[Row] =
      val path = s"./dataframes/${name.name}.parquet"
      sparkSession.read.parquet(path)

  /** Provides an implicit `Writer[Dataset[Row]]` instance.
    *
    * This instance can write `Dataset[Row]` instances as JSON by converting
    * them to `DataFrameName` instances using the `writeAsParquet` method.
    */
  given Writer[Dataset[Row]] =
    macroRW[DataFrameName]
    .comap(_.toDataFrameName)


  /** Provides an implicit `Reader[Dataset[Row]]` instance.
    *
    * This instance can read `Dataset[Row]` instances from JSON by converting
    * `DataFrameName` instances to datasets using the `readAsDataFrame` method.
    */
  given Reader[Dataset[Row]] =
    macroRW[DataFrameName]
    .map(_.readAsDataFrame)
end DataFrameName