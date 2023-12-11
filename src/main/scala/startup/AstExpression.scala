package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}//, reader, writer}
import org.apache.spark.sql.{Dataset, Row}

trait Validator[T]:
  def validate(t: T): Boolean
object Validator:
  given Validator[Int] with
    def validate(x: Int): Boolean = x >= 0
  given Validator[Double] with
    def validate(x: Double): Boolean = x >= 0
  given Validator[Dataset[Row]] with
    def validate(df: Dataset[Row]): Boolean = df.count < 1000


/** A sealed trait representing an arithmetic expression.
  */
sealed trait Expression[T]

/** A case class representing a number in an arithmetic expression.
  */
//case class Num[T: Validator](n: T) extends Expression[T]:
//  require(summon[Validator[T]].validate(n), "Validation failed for the given type")
case class Num[T] private (n: T) extends Expression[T]
object Num:
  def apply[T: Validator](n: T): Num[T] =
    require(summon[Validator[T]].validate(n), "Validation failed for the given type")
    new Num(n)

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
      case Num[T](num)   => num
      case Mult[T](a, b) => ops.mul(evaluate(a), evaluate(b))
      case Plus[T](a, b) => ops.add(evaluate(a), evaluate(b))
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
  given [T](using RW[T]): RW[Expression[T]] =
    RW.merge(
      macroRW[Num[T]],
      macroRW[Plus[T]],
      macroRW[Mult[T]]
    )
/*
  given [T: Reader: Validator]: Reader[Num[T]] with
    def read(value: String): Num[T] =
      val numt = read(value)
      require(summon[Validator[T]].validate(numt.n), "Validation failed for the given type")
      numt

  given [T: Reader: Validator]: Reader[Plus[T]] with
    def read(value: String): Plus[T] = read(value)
  given [T: Reader: Validator]: Reader[Mult[T]] with
    def read(value: String): Mult[T] = read(value)

  given [T: Reader: Validator]: Reader[Expression[T]] =
    Reader.merge[Expression[T]](
      reader[Num[T]],
      reader[Plus[T]],
      reader[Mult[T]]
    )
  
  given [T: Writer: Validator]: Writer[Num[T]] with
    def write(numt: Num[T]): String = write(numt)
  
  given [T: Writer: Validator]: Writer[Plus[T]] with
    def write(plust: Plus[T]): String = write(plust)
  
  given [T: Writer: Validator]: Writer[Mult[T]] with
    def write(mult: Mult[T]): String = write(mult)

  given [T: Writer: Validator]: Writer[Expression[T]] =
    Writer.merge[Expression[T]](
      writer[Num[T]],
      writer[Plus[T]],
      writer[Mult[T]]
    )
*/
  def validateExpression[T](
      expression: Expression[T]
  ): Either[String, Expression[T]] =
    expression match
      case Num(n) => Right(expression)
      case Mult(a, b) =>
        (validateExpression(a), validateExpression(b)) match
          case (Right(_), Right(_)) => Right(expression)
          case _                    => Left("Invalid multiplication expression")
      case Plus(a, b) =>
        (validateExpression(a), validateExpression(b)) match
          case (Right(_), Right(_)) => Right(expression)
          case _                    => Left("Invalid addition expression")

end Expression