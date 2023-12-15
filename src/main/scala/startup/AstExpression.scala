package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}
import org.apache.spark.sql.{Dataset, Row}

/** A sealed trait representing an arithmetic expression.
  */
sealed trait Expression[T]

/** A case class representing an operand in an arithmetic expression.
  */
case class Num[T](n: T) extends Expression[T]

/** A case class representing a kind of multiplication operation in an
  * arithmetic expression.
  */
case class Mult[T](a: Expression[T], b: Expression[T]) extends Expression[T]

/** A case class representing a kind of addition operation in an arithmetic
  * expression.
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

  /** Validates an arithmetic expression.
    *
    * @param expression
    *   The expression to validate.
    * @return
    *   The result of the validation.
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

end Expression

/** A trait that defines a method to validate an argument.
  */
trait ArgumentValidator[T]:
  def validate(t: T): Boolean

/** Companion object for `ArgumentValidator` that provides given instances for
  * `Int`, `Double`, and `Dataset[Row]` types.
  */
object ArgumentValidator:
  given ArgumentValidator[Int] with
    def validate(x: Int): Boolean = x > 3
  given ArgumentValidator[Double] with
    def validate(x: Double): Boolean = x > 0
  given ArgumentValidator[Dataset[Row]] with
    def validate(df: Dataset[Row]): Boolean =
      val dfcount: Long = df.count
      (dfcount > 2) && (dfcount < 1000)
