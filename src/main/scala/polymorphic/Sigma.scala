package polymorphic

import cats.data.{EitherK, Tuple2K}

sealed abstract class Sigma[+A, F[_]] { fa =>
  val first: A
  val second: F[first.type]

  final def mapK[G[_]](fg: F ~> G): Sigma[A, G] =
    Sigma(first)(fg.apply(second))

  final def widen[B >: A]: Sigma[B, F] = this

  final def widenK[G[x] >: F[x]]: Sigma[A, G] =
    this.asInstanceOf[Sigma[A, G]]
}

object Sigma {
  def apply[A, F[_]](a: A)(fa: F[a.type]): Sigma[A, F] =
    new Sigma[A, F] {
      val first: A = a
      val second: F[first.type] = fa.asInstanceOf[F[first.type]]
    }

  final def flip[A, B, F[_, _]](f: Sigma[A, λ[a => Sigma[B, λ[b => F[a, b]]]]]): Sigma[B, λ[b => Sigma[A, λ[a => F[a, b]]]]] =
    Sigma[B, λ[b => Sigma[A, λ[a => F[a, b]]]]](f.second.first)(
      Sigma[A, λ[a => F[a, f.second.first.type]]](f.first)(f.second.second))

  final def or[A, F[_], G[_]](fg: Either[Sigma[A, F], Sigma[A, G]]): Sigma[A, EitherK[F, G, ?]] =
    fg match {
      case Left(af) => Sigma[A, EitherK[F, G, ?]](af.first)(EitherK.leftc(af.second))
      case Right(ag) => Sigma[A, EitherK[F, G, ?]](ag.first)(EitherK.rightc(ag.second))
    }

  final def const[A, B](a: A)(b: B): Sigma[B, λ[b => a.type]] =
    Sigma[B, λ[b => a.type]](b)(a)

  final def id[A](a: A): Sigma[A, λ[a => a]] =
    Sigma[A, λ[a => a]](a)(a)

  def summon[F[_]]: PartialSummon[F] = new PartialSummon[F]

  final class PartialSummon[F[_]](val b: Boolean = true) extends AnyVal {
    def apply[A](a: A)(implicit F: F[a.type]): Sigma[A, F] =
      apply(a)(F)
  }
}