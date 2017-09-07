package polymorphic

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

    final def of[F[_]]: MkForall[F] =
        new MkForall[F]
    final def apply[F[_]]: MkForall[F] =
        new MkForall[F]
}

final class MkForall[F[_]](val b: Boolean = true) extends AnyVal {
    type T

    def apply(ft: F[T]): ∀[F] =
        from(ft)

    def from(ft: F[T]): ∀[F] =
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
}