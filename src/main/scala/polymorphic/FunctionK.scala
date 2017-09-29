package polymorphic

import cats.arrow.FunctionK

private[polymorphic] final class WrappedFK[T, F[_], G[_]](f: F[T] => G[T]) extends FunctionK[F, G] {
    override def apply[A](fa: F[A]): G[A] =
        f(fa.asInstanceOf[F[T]]).asInstanceOf[G[A]]
}

private[polymorphic]
final class MkFunctionK(val dummy: Boolean = true) extends AnyVal {
    type T
    def apply[F[_], G[_]](f: F[T] => G[T]): F ~> G = new WrappedFK[T, F, G](f)

    def applyT[F[_], G[_]](f: Null { type Type = T } => (F[T] => G[T])): F ~> G = {
        val ff = f(null.asInstanceOf[Null { type Type = T }])
        new WrappedFK[T, F, G](ff)
    }
}