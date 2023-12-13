package startup.ast

import upickle.default.{
  ReadWriter => RW,
  macroRW,
  Reader,
  Writer
}
import org.apache.spark.sql.{Dataset, Row}

trait ArgumentValidator[T]:
  def validate(t: T): Boolean
object ArgumentValidator:
  given ArgumentValidator[Int] with
    def validate(x: Int): Boolean = x > 3
  given ArgumentValidator[Double] with
    def validate(x: Double): Boolean = x > 0
  given ArgumentValidator[Dataset[Row]] with
    def validate(df: Dataset[Row]): Boolean =
      val dfcount: Long = df.count
      (dfcount > 2) && (dfcount < 1000)

/** A sealed trait representing an arithmetic expression.
  */
sealed trait Expression[T]

/** A case class representing a number in an arithmetic expression.
  */
//case class Num[T: Validator](n: T) extends Expression[T]:
//  require(summon[Validator[T]].validate(n), "Validation failed for the given type")
/*
case class Num[T] private (n: T) extends Expression[T]
object Num:
  def apply[T: Validator](n: T): Num[T] =
    require(summon[Validator[T]].validate(n), "Validation failed for the given type")
    new Num(n)
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
  //implementation without validation
  //def evaluate[T](expression: Expression[T])(using
  //    ops: ArithmeticOperation[T]
  //): T =
  //  expression match
  //    case Num[T](num)   => num
  //    case Mult[T](a, b) => ops.mul(evaluate(a), evaluate(b))
  //    case Plus[T](a, b) => ops.add(evaluate(a), evaluate(b))
  //end evaluate

  def evaluateValidExpression[T: OperationValidator](
      expression: Either[List[String], Expression[T]]
  )(using
      ops: ArithmeticOperation[T]
  ): Either[List[String], T] =
    try
      expression match
        case Right(exp) =>
          exp match
            case Num[T](num) => Right(num)
            case Mult[T](a, b) =>
              for
                A <- evaluateValidExpression(Right(a))
                B <- evaluateValidExpression(Right(b))
              yield
                if summon[OperationValidator[T]].validate(A, B, "mul") then
                  ops.mul(A, B)
                else throw new Exception("Invalid mul operation")
            case Plus[T](a, b) =>
              for
                A <- evaluateValidExpression(Right(a))
                B <- evaluateValidExpression(Right(b))
              yield
                if summon[OperationValidator[T]].validate(A, B, "add") then
                  ops.add(A, B)
                else throw new Exception("Invalid plus operation")
        case Left(errors) => Left(errors)
    catch case error => Left(List(s"$error"))
  end evaluateValidExpression

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
  def validateExpression[T: ArgumentValidator](
      expression: Expression[T]
  ): Either[List[String], Expression[T]] =
    def valExpIntern[T: ArgumentValidator](
        expression: Expression[T],
        errorsAccumulator: List[String]
    ): Either[List[String], Expression[T]] =
      try
        expression match
          case Num(n) =>
            if summon[ArgumentValidator[T]].validate(n) then Right(expression)
            else Left(errorsAccumulator :+ s"Invalid Num argument $n")
          case Mult(a, b) =>
            (
              valExpIntern(a, errorsAccumulator),
              valExpIntern(b, errorsAccumulator)
            ) match
              case (Right(_), Right(_)) => Right(expression)
              case (Left(error), Right(_)) =>
                Left(errorsAccumulator ++ error)
              case (Right(_), Left(error)) =>
                Left(errorsAccumulator ++ error)
              case (Left(error1), Left(error2)) =>
                Left(
                  errorsAccumulator ++ error1 ++ error2
                )
          case Plus(a, b) =>
            (
              valExpIntern(a, errorsAccumulator),
              valExpIntern(b, errorsAccumulator)
            ) match
              case (Right(_), Right(_)) => Right(expression)
              case (Left(error), Right(_)) =>
                Left(errorsAccumulator ++ error)
              case (Right(_), Left(error)) =>
                Left(errorsAccumulator ++ error)
              case (Left(error1), Left(error2)) =>
                Left(
                  errorsAccumulator ++ error1 ++ error2
                )
          case null => Left(errorsAccumulator ++ List(s"null expression"))
      catch
        case error =>
          Left(errorsAccumulator :+ s"error in expression validation: $error")
    end valExpIntern
    valExpIntern(expression, List.empty[String])
  end validateExpression
/*
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
      case null => Left("this is a null expression")
 */
end Expression
