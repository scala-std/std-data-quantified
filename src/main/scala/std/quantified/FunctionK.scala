package std.quantified

trait FunctionK[F[_], G[_]] { fg =>
  def apply[A](fa: F[A]): G[A]

  def andThen[H[_]](gh: FunctionK[G, H]): FunctionK[F, H] =
    new FunctionK[F, H] {
      override def apply[A](fa: F[A]): H[A] = gh(fg(fa))
    }

  def toForall: Forall[λ[x => F[x] => G[x]]] =
    Forall[λ[x => F[x] => G[x]]](x => fg.apply(x))
}
object FunctionK {
  def id[F[_]]: FunctionK[F, F] = new FunctionK[F, F] {
    override def apply[A](fa: F[A]): F[A] = fa
  }
}

private[quantified] final class WrappedFK[T, F[_], G[_]](f: F[T] => G[T]) extends FunctionK[F, G] {
  override def apply[A](fa: F[A]): G[A] =
    f(fa.asInstanceOf[F[T]]).asInstanceOf[G[A]]
}

private[quantified]
final class MkFunctionK(val dummy: Boolean = true) extends AnyVal {
  type T
  def apply[F[_], G[_]](f: F[T] => G[T]): F ~> G = new WrappedFK[T, F, G](f)

  def applyT[F[_], G[_]](f: Null { type Type = T } => (F[T] => G[T])): F ~> G = {
    val ff = f(null.asInstanceOf[Null { type Type = T }])
    new WrappedFK[T, F, G](ff)
  }
}

//object FunctionK {
//  type Base <: AnyRef
//  trait Tag
//  type Type[F[_], G[_]] <: Base with Tag
//
//  implicit final class Ops[F[_], G[_]](val fg: Type[F, G]) extends AnyVal {
//    def specialize[A]: F[A] => G[A] =
//      fg.asInstanceOf[F[A] => G[A]]
//
//    def apply[A](fa: F[A]): G[A] =
//      specialize[A](fa)
//
//    def toForall: Forall[λ[x => F[x] => G[x]]] =
//      Forall.witness[λ[x => F[x] => G[x]]](specialize)
//  }
//
//  implicit final class $MkFunctionK(val t: FunctionK.type) extends AnyVal {
//    type Hidden
//
//    def witness[F[_], G[_]](fa: F[Hidden] => G[Hidden]): Type[F, G] =
//      fa.asInstanceOf[Type[F, G]]
//
//    def witnessT[F[_], G[_]](ft: TypeHolder[Hidden] => F[Hidden] => G[Hidden]): Type[F, G] =
//      witness(ft(TypeHolder[Hidden]))
//  }
//}