package polymorphic.syntax

import polymorphic.{Exists, ~>}

object ExistsSyntax {
    final class ExistsOps[F[_], A](val fa: Exists[F] { type T = A }) extends AnyVal {
        def value: F[A] =
            Exists.unwrap[F](fa)

        def mapK[G[_]](fg: F ~> G): Exists[G] =
            Exists.mapK[F, G](fa)(fg)

        def toScala: F[A] forSome { type A } =
            Exists.unwrap[F](fa)
    }
}
trait ExistsSyntax {
    import ExistsSyntax._

    implicit final def toExistsOps[F[_], A]
    (fa: Exists[F] {type T = A}): ExistsOps[F, A] =
        new ExistsOps[F, A](fa)
}