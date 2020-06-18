package std.data.quantified

import scala.reflect.ClassTag
import scala.reflect.macros.{TypecheckException, blackbox}

trait Prototype[+F[_]] {
  def apply[X]: F[X]
}

private[quantified] sealed trait Forall1 {
  import Forall._
  type Base
  type Type[+F[_]] <: Base with Tag
}
private[quantified] sealed trait Forall2 {
  sealed trait Tag extends Any
}
object Forall extends Forall1 with Forall2 {
  final def specialize[F[_], A](v: Type[F]): F[A] =
    v.asInstanceOf[F[A]]

  final def from[F[_]](p: Prototype[F]): Type[F] =
    ∀[F].apply(p.apply)

  final def mapK[F[_], G[_]](f: Type[F])(fg: F ~> G): Type[G] =
    ∀[G].apply(fg.apply(specialize(f)))

  final def and[F[_], G[_]](f: ∀[F], g: ∀[G]): ∀[λ[x => (F[x], G[x])]] =
    ∀[λ[x => (F[x], G[x])]].apply((specialize(f), specialize(g)))

  final def or[F[_], G[_]](fg: Either[∀[F], ∀[G]]): ∀[λ[x => F[x] Either G[x]]] =
    ∀[λ[x => F[x] Either G[x]]].applyT { T =>
      fg match {
        case Left(f) => Left(specialize(f))
        case Right(g) => Right(specialize(g))
      }
    }

  final def lift[F[_], G[_]](f: ∀[F]): ∀[λ[X => F[G[X]]]] =
    ∀[λ[X => F[G[X]]]].apply(specialize(f))

  final def const[A](a: A): ∀[λ[X => A]] =
    ∀[λ[X => A]].apply(a)

  final def of[F[_]]: MkForall1[F] =
    new MkForall1[F]

  implicit def classTag[F[_]]: ClassTag[Type[F]] =
    ClassTag.Any.asInstanceOf[ClassTag[Type[F]]]

  sealed trait $Hidden extends Any
  implicit def mkForall[F[_]](implicit v: F[$Hidden]): Type[F] =
    v.asInstanceOf[Type[F]]
}

private[quantified]
final class MkForall1[F[_]](val b: Int = 0) extends AnyVal {
  type T

  @inline def apply(ft: F[this.T]): ∀[F] =
    from(ft)

  @inline def applyT(ft: Null { type Type = T } => F[T]): ∀[F] =
    from(ft(null.asInstanceOf[Null { type Type = T }]))

  @inline def from(ft: F[this.T]): ∀[F] =
    ft.asInstanceOf[∀[F]]
}

private[quantified]
final class MkForall(val b: Boolean = true) extends AnyVal {
  type T

  @inline def apply[F[_]](ft: F[this.T]): ∀[F] =
    ft.asInstanceOf[∀[F]]

  @inline def applyT[F[_]](ft: Null { type Type = T } => F[T]): ∀[F] =
    apply(ft(null.asInstanceOf[Null { type Type = T }]))
}

//object Forall {
//  type Base
//  trait Tag extends Any
//  type Type[F[_]]    <: Base with Tag
//
//  def apply[F[_]](implicit ev: Type[F]): Type[F] = ev
//
//
//  implicit final class $MkForall(val t: Forall.type) extends AnyVal {
//    type Hidden
//
//    def witness[F[_]](fa: F[Hidden]): Type[F] =
//      fa.asInstanceOf[Type[F]]
//
//    def witnessT[F[_]](ft: TypeHolder[Hidden] => F[Hidden]): Type[F] =
//      witness(ft(TypeHolder[Hidden]))
//  }
//
//  implicit final class ForallOps[F[_]](val fx: Type[F]) extends AnyVal {
//    def apply[A]: F[A] = fx.asInstanceOf[F[A]]
//
//    def toFunctionK[G[_], H[_]](implicit p: F =~= λ[x => G[x] => H[x]]): G ~> H =
//      FunctionK.witnessT[G, H] { t =>
//        p.apply(fx.apply[t.Type])
//      }
//  }
//
//  implicit def proposition[F[_]](implicit ev: Forall[λ[x => Proposition[F[x]]]]): Proposition[Forall[F]] =
//    (A: ¬¬[Forall[F]]) => Forall.witnessT { t =>
//      val p: ¬¬[F[t.Type]] = A.map(_.apply[t.Type])
//      ev.apply[t.Type].proved(p)
//    }
//}
