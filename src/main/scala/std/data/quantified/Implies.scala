package std.data.quantified

import scala.reflect.macros.{TypecheckException, blackbox}

object Implies {
  type Base <: AnyRef
  type Tag
  type Type[A, B] <: Base with Tag

  def apply[A, B](implicit ev: Type[A, B]): Type[A, B] = ev

  implicit def mkImplies[A, B]: Type[A, B] =
    macro ImplicationMacro.mkImplies[A, B]

  implicit def run[A, B](implicit A: A, AB: A |- B): B =
    AB.asInstanceOf[A => B].apply(A)
}


final class ImplicationMacro(val c: blackbox.Context) {
  import c.universe._

  def mkImplies[A: c.WeakTypeTag, B: c.WeakTypeTag]: c.Tree = {
    val A = weakTypeOf[A]
    val B = weakTypeOf[B]

    val dummy0 = TermName(c.freshName)

    try c.typecheck(q"""
        {
          def run(implicit $dummy0: $A): $B = implicitly[$B]
          ((x: $A) => run(x)).asInstanceOf[_root_.std.quantified.Implies.Type[$A, $B]]
        }
      """)
    catch {
      case TypecheckException(_, msg) =>
        c.abort(c.enclosingPosition, s"Could not prove that $A implies $B: $msg")
      case e: Throwable => throw e
    }
  }
}