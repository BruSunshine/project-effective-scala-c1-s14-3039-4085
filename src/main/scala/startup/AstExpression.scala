package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}

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
  given [T](using RW[T]): RW[Expression[T]] = RW.merge(
    macroRW[Num[T]],
    macroRW[Plus[T]],
    macroRW[Mult[T]]
  )

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