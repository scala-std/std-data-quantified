package polymorphic

import cats.data.{EitherK, Tuple2K}

import scala.reflect.ClassTag

trait Prototype[+F[_]] {
    def apply[X]: F[X]
}

private[polymorphic] sealed trait Forall1 {
    import Forall._
    type Base
    type Type[+F[_]] <: Base with Tag
}
private[polymorphic] sealed trait Forall2 {
    sealed trait Tag extends Any
}
object Forall extends Forall1 with Forall2 {
    final def specialize[F[_], A](v: Type[F]): F[A] =
        v.asInstanceOf[F[A]]

    final def from[F[_]](p: Prototype[F]): Type[F] =
        ∀[F].apply(p.apply)

    final def mapK[F[_], G[_]](f: Type[F])(fg: F ~> G): Type[G] =
        ∀[G].apply(fg.apply(specialize(f)))

    final def and[F[_], G[_]](f: ∀[F], g: ∀[G]): ∀[Tuple2K[F, G, ?]] =
        ∀[Tuple2K[F, G, ?]].apply(Tuple2K(specialize(f), specialize(g)))

    final def or[F[_], G[_]](fg: Either[∀[F], ∀[G]]): ∀[EitherK[F, G, ?]] =
        ∀[EitherK[F, G, ?]].applyT { T =>
            fg match {
                case Left(f) => EitherK.leftc[F, G, T.Type](specialize(f))
                case Right(g) => EitherK.rightc[F, G, T.Type](specialize(g))
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
}

private[polymorphic]
final class MkForall1[F[_]](val b: Int = 0) extends AnyVal {
    type T

    @inline def apply(ft: F[this.T]): ∀[F] =
        from(ft)

    @inline def applyT(ft: Null { type Type = T } => F[T]): ∀[F] =
        from(ft(null.asInstanceOf[Null { type Type = T }]))

    @inline def from(ft: F[this.T]): ∀[F] =
        ft.asInstanceOf[∀[F]]
}

private[polymorphic]
final class MkForall(val b: Boolean = true) extends AnyVal {
    type T

    @inline def apply[F[_]](ft: F[this.T]): ∀[F] =
        ft.asInstanceOf[∀[F]]

    @inline def applyT[F[_]](ft: Null { type Type = T } => F[T]): ∀[F] =
        apply(ft(null.asInstanceOf[Null { type Type = T }]))
}