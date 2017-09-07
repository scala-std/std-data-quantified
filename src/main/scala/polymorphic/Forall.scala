package polymorphic

import cats.data.{EitherK, Tuple2K}

trait Prototype[+F[_]] {
    def apply[X]: F[X]
}

private[polymorphic]
sealed abstract class ForallT {
    // Basically `Any`.
    type ∀[F[_]]

    def specialize[F[_], A](v: ∀[F]): F[A]
    def from[F[_]](p: Prototype[F]): ∀[F]

    def mapK[F[_], G[_]](f: ∀[F])(fg: F ~> G): ∀[G]
    def and[F[_], G[_]](f: ∀[F], g: ∀[G]): ∀[λ[X => Tuple2K[F, G, X]]]
    def or[F[_], G[_]](f: Either[∀[F], ∀[G]]): ∀[λ[X => EitherK[F, G, X]]]
    def lift[F[_], G[_]](f: ∀[F]): ∀[λ[X => F[G[X]]]]

    final def of[F[_]]: MkForall1[F] =
        new MkForall1[F]

    def const[A](a: A): ∀[λ[X => A]]
}

private[polymorphic]
final class MkForall1[F[_]](val b: Int = 0) extends AnyVal {
    type T

    def apply(ft: F[this.T]): ∀[F] =
        from(ft)

    def from(ft: F[this.T]): ∀[F] =
        ft.asInstanceOf[∀[F]]
}

private[polymorphic]
final class MkForall(val b: Boolean = true) extends AnyVal {
    type T

    def apply[F[_]](ft: F[this.T]): ∀[F] =
        ft.asInstanceOf[∀[F]]
}

private[polymorphic]
final class ForallImpl extends ForallT {
    type ∀[F[_]] = F[Any]

    def specialize[F[_], A](v: ∀[F]): F[A] =
        v.asInstanceOf[F[A]]

    def from[F[_]](p: Prototype[F]): ∀[F] =
        p.apply[Any]

    def mapK[F[_], G[_]](f: ∀[F])(fg: F ~> G): ∀[G] =
        fg.apply[Any](f)
    def and[F[_], G[_]](f: ∀[F], g: ∀[G]): ∀[λ[X => Tuple2K[F, G, X]]] =
        Tuple2K[F, G, Any](f, g)
    def or[F[_], G[_]](fg: Either[∀[F], ∀[G]]): ∀[λ[X => EitherK[F, G, X]]] = fg match {
        case Left(f) => EitherK.leftc[F, G, Any](f)
        case Right(g) => EitherK.rightc[F, G, Any](g)
    }
    def lift[F[_], G[_]](f: ∀[F]): ∀[λ[X => F[G[X]]]] =
        f.asInstanceOf[F[G[Any]]]

    def const[A](a: A): ∀[λ[X => A]] = a
}