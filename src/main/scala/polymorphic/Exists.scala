package polymorphic

import scala.reflect.ClassTag

private[polymorphic] sealed trait Exists1 {
    import Exists._
    type Type[+F[_]] <: (Any { type T }) with Tag
}
sealed trait Exists2 {
    trait Tag extends Any
}
object Exists extends Exists1 with Exists2 {
    def wrap[F[_], A](value: F[A]): Type[F] =
        value.asInstanceOf[Type[F]]

    def unwrap[F[_]](value: Type[F]): F[value.T] =
        value.asInstanceOf[F[value.T]]

    def apply[F[_], A](fa: F[A]): Type[F] =
        wrap[F, A](fa)

    def wrapE[F[_]](value: F[T] forSome { type T }): Type[F] =
        wrap(value)

    def unwrapE[F[_]](value: Type[F]): F[T] forSome { type T } =
        unwrap[F](value)

    def mapK[F[_], G[_]](tf: Type[F])(fg: F ~> G): Type[G] =
        wrap(fg.apply(unwrap(tf)))

    def from[F[_]](instance: Instance[F]): Type[λ[X => (X, F[X])]] =
        apply[λ[X => (X, F[X])], instance.Type]((instance.first, instance.second))

    def unapply[F[_]](value: Type[F]): Option[F[value.T]] =
        Some(unwrap(value))

    implicit def classTag[F[_]]: ClassTag[Type[F]] =
        ClassTag.Any.asInstanceOf[ClassTag[Type[F]]]
}

private[polymorphic]
final class MkExists1[F[_]](val b: Boolean = true) extends AnyVal {
    @inline def apply[A](ft: F[A]): ∃[F] =
        Exists.wrap[F, A](ft)
}

private[polymorphic]
final class MkExists(val b: Int = 0) extends AnyVal {
    @inline def apply[F[_], A](ft: F[A]): ∃[F] =
        Exists.wrap[F, A](ft)
}