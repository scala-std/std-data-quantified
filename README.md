# Polymorphic

 * `Exists[F[_]]` witnesses that there exists some type `A` and a value of
   type `F[A]`. For instance, if you want to witness that a some type
   `T` has an instance of `Show[T]`, you can provide
   `Exists[λ[α => (α, Show[α])]]`.
 * `Forall[F[_]]` is a universal quantification encoded as a type in Scala.
   If you want to witness that some type `F[_]` has a monoid instance
   regardless of the type argument, you can provide
   `Forall[λ[α => Monoid[F[α]]]]`.
  * `Instance[F[_]]` is a more convenient version of `Exists[λ[α => (α, F[α])]]` 
   that can be resolved implicitly (see example below).

## Quick Start
```scala
resolvers += Resolver.bintrayRepo("alexknvl", "maven")
libraryDependencies += "com.alexknvl"  %%  "polymorphic" % "0.1.0"
```

```scala
import polymorphic._
import polymorphic.syntax._

def bar(a: Instance[Show]*): String =
    a.map(x => x.second.show(x.first)).mkString(", ")
bar(1, 2, 3) // "1, 2, 3"

class Foo[A]
val foo: ∀[Foo] = ∀(new Foo)
foo[Int] // : Foo[Int]

class Baz[A](val x: A) {
    def show(a: A): String = a.toString
}
val baz: ∃[Baz] = ∃(new Baz(1))
baz.value.show(baz.value.x) // "1"
baz match { case Exists(f) => f.show(f.value) } // "1"

val optToList: Option ~> List = FunctionK(_.toList)
val listToOpt = FunctionK[List, Option](_.headOption)
```

## License
Code is provided under the MIT license available at https://opensource.org/licenses/MIT,
as well as in the LICENSE file.
