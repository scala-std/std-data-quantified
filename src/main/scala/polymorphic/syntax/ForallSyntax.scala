package polymorphic.syntax

import polymorphic.{Forall, ~>}

object ForallSyntax {
    final class Ops[F[_]](val f: Forall[F]) extends AnyVal {
        def apply[X]: F[X] =
            Forall.specialize[F, X](f)

        def mapK[G[_]](fg: F ~> G): Forall[G] =
            Forall.mapK[F, G](f)(fg)
    }
}

trait ForallSyntax {
    import ForallSyntax._

    implicit def toForallOps[F[_]](f: Forall[F]): Ops[F] =
        new Ops[F](f)
}
