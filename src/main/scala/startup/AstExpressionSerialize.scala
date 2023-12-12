package startup.ast

import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer}//, reader, writer}
//import scala.util.Try
import startup.ast.Expression.given

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
    argast: Either[List[String], Expression[T]]
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
  
  //given [T: RW: Validator]: RW[ExpressionToSerialize[T]] =
  //  macroRW[ExpressionToSerialize[T]]
/*
  given [T: Reader: Writer]: Reader[Either[String, Expression[T]]] with
    def read(value: String): Either[String, Expression[T]] =
      Try(summon[Reader[Expression[T]]].read(value)).toEither.left.map(_ => value)
  
  given [T: Reader: Writer: Validator]: Writer[Either[String, Expression[T]]] with
    def write(either: Either[String, Expression[T]]): String = 
      either match
        case Left(s) => s
        case Right(expr) => summon[Writer[Expression[T]]].write(expr)
*/  
  //given [T: Reader]: Reader[Either[String, Expression[T]]] with
  //  def read(value: String): Either[String, Expression[T]] =
  //    read(value)
  //given [T: Writer]: Writer[Either[String, Expression[T]]] with
  //  def write(expr: Expression[T]): Either[String, Expression[T]] =
  //    write(expr)
/*
  given [T: Reader: Writer]: Writer[Either[String, Expression[T]]] with
    def write(either: Either[String, Expression[T]]): Value = 
      either match {
        case Left(s) => summon[Writer[String]].write(s)
        case Right(expr) => summon[Writer[Expression[T]]].write(expr)
    }
*/
/*
  // need to review necessity of this additional given definition
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
    */


end ExpressionToSerialize