package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}
import startup.ast.Expression.given

/** A case class that wraps an `Expression[T]` instance.
  *
  * This class is used to serialize `Expression[T]` instances. It takes an `Either` type
  * which can be a list of strings representing errors or an `Expression[T]` instance.
  *
  * @param argast
  *   The `Expression[T]` instance to serialize or a list of errors.
  * @tparam T
  *   The type of the values in the expression.
  */
case class ExpressionToSerialize[T](
    argast: Either[List[String], Expression[T]]
)

/** Companion object for `ExpressionToSerialize` that provides an implicit
  * `ReadWriter` instance.
  *
  * The `ReadWriter` instance is used to serialize and deserialize `ExpressionToSerialize[T]`
  * instances to and from JSON. It uses the `macroRW` macro from the `upickle` library to
  * automatically generate the `ReadWriter` instance.
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

end ExpressionToSerialize
