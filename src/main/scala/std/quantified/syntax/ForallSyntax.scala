package std.quantified.syntax

import std.quantified.{Forall, ~>, ∀}

object ForallSyntax {
    final class Ops[F[_]](val f: ∀[F]) extends AnyVal {
        def apply[X]: F[X] =
            Forall.specialize[F, X](f)

        def mapK[G[_]](fg: F ~> G): ∀[G] =
            Forall.mapK[F, G](f)(fg)

        def and[G[_]](g: ∀[G]): ∀[λ[x => (F[x], G[x])]] =
            Forall.and[F, G](f, g)

        def lift[G[_]]: ∀[λ[X => F[G[X]]]] =
            Forall.lift[F, G](f)
    }
}

trait ForallSyntax {
    import ForallSyntax._

    implicit def toForallOps[F[_]](f: Forall[F]): Ops[F] =
        new Ops[F](f)
}
