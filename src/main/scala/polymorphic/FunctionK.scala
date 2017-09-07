package polymorphic

private[polymorphic] final class MkFunctionK(val dummy: Boolean = true) extends AnyVal {
    type T
    def apply[F[_], G[_]](f: F[T] => G[T]): F ~> G = new (F ~> G) {
        def apply[A](fa: F[A]): G[A] = f(fa.asInstanceOf[F[T]]).asInstanceOf[G[A]]
    }
}