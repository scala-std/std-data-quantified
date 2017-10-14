package polymorphic

import cats.data.{EitherK, Tuple2K}

import scala.reflect.ClassTag


trait Pi[-A, F[_]] { fa =>
  def apply(a: A): F[a.type]

  final def mapK[G[_]](fg: F ~> G): Pi[A, G] =
    new Pi[A, G] {
      override def apply(a: A): G[a.type] = fg.apply(fa.apply(a))
    }

  final def narrow[B <: A]: Pi[B, F] = this

  final def widenK[G[x] >: F[x]]: Pi[A, G] =
    this.asInstanceOf[Pi[A, G]]
}

object Pi {
  final def and[A, F[_], G[_]](f: Pi[A, F], g: Pi[A, G]): Pi[A, Tuple2K[F, G, ?]] =
    new Pi[A, Tuple2K[F, G, ?]] {
      override def apply(a: A): Tuple2K[F, G, a.type] =
        Tuple2K(f.apply(a), g.apply(a))
    }

  final def flip[A, B, F[_, _]](f: Pi[A, λ[a => Pi[B, λ[b => F[a, b]]]]]): Pi[B, λ[b => Pi[A, λ[a => F[a, b]]]]] =
    new Pi[B, λ[b => Pi[A, λ[a => F[a, b]]]]] {
      override def apply(b: B): Pi[A, λ[a => F[a, b.type]]] = new Pi[A, λ[a => F[a, b.type]]] {
        override def apply(a: A): F[a.type, b.type] = f.apply(a).apply(b)
      }
    }

  final def or[A, F[_], G[_]](fg: Either[Pi[A, F], Pi[A, G]]): Pi[A, EitherK[F, G, ?]] =
    new Pi[A, EitherK[F, G, ?]] {
      override def apply(a: A): EitherK[F, G, a.type] = fg match {
        case Left(af) => EitherK.leftc(af.apply(a))
        case Right(ag) => EitherK.rightc(ag.apply(a))
      }
    }

  final def const[A, B](a: A): Pi[B, λ[b => a.type]] =
    new Pi[B, λ[b => a.type]] {
      override def apply(b: B): a.type = a
    }

  final def id[A]: Pi[A, λ[a => a]] =
    new Pi[A, λ[a => a]] {
      override def apply(a: A): a.type = a
    }
}