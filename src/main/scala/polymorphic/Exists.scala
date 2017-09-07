package polymorphic

private[polymorphic]
final class ExistsT {
    type ∃[+F[_]] <: Any { type T }

    def apply[F[_], A](fa: F[A]): ∃[F] =
        wrap[F, A](fa)

    def wrap[F[_], A](value: F[A]): ∃[F] =
        value.asInstanceOf[∃[F]]

    def unwrap[F[_]](value: ∃[F]): F[value.T] =
        value.asInstanceOf[F[value.T]]

    def wrapE[F[_]](value: F[T] forSome { type T }): ∃[F] =
        value.asInstanceOf[∃[F]]

    def unwrapE[F[_]](value: ∃[F]): F[T] forSome { type T } =
        unwrap[F](value)

    def mapK[F[_], G[_]](tf: ∃[F])(fg: F ~> G): ∃[G] =
        fg.apply(tf.asInstanceOf[F[Any]]).asInstanceOf[∃[G]]

    def from[F[_]](instance: Instance[F]): ∃[λ[X => (X, F[X])]] =
        apply[λ[X => (X, F[X])], instance.Type]((instance.first, instance.second))
}