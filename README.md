# Polymorphic

 * `Exists[F[_]]` witnesses that there exists some type `A` and a value of
   type `F[A]`. For instance, if you want to witness that a some type
   `T` has an instance of `Show[T]`, you can provide
   `Exists[λ[α => (α, Show[α])]]`.
 * `Forall[F[_]]` is a universal quantification encoded as a type in Scala.
   If you want to witness that some type `F[_]` has a monoid instance
   regardless of the type argument, you can provide
   `Forall[λ[α => Monoid[F[α]]]]`.

## Quick Start
```scala
resolvers += Resolver.bintrayRepo("alexknvl", "maven")
libraryDependencies += "com.alexknvl"  %%  "polymorphic" % "0.1.0"
```

## License
Code is provided under the MIT license available at https://opensource.org/licenses/MIT,
as well as in the LICENSE file.
