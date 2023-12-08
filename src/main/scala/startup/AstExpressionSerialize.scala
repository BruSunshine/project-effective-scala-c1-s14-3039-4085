package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}

/** A case class that wraps an `Expression[T]` instance.
  *
  * This class is used to serialize `Expression[T]` instances.
  *
  * @param argast
  *   The `Expression[T]` instance to serialize.
  * @tparam T
  *   The type of the values in the expression.
  */
case class ExpressionToSerialize[T](
    argast: Either[String, Expression[T]]
)

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

  given [T](using rws: RW[String], rwt: RW[T])(using
      rwExT: RW[ExpressionToSerialize[T]]
  ): RW[Either[String, ExpressionToSerialize[T]]] =
    RW.merge(
      rws.bimap[Either[String, ExpressionToSerialize[T]]](
        {
          case Left(str) => str
          case Right(_)  => throw new Exception("Not a Left value")
        },
        str => Left(str)
      ),
      rwExT.bimap[Either[String, ExpressionToSerialize[T]]](
        {
          case Right(expr) => expr
          case Left(_)     => throw new Exception("Not a Right value")
        },
        expr => Right(expr)
      )
    )

end ExpressionToSerialize