# Polymorphic
Un-boxed existential and universal quantifiers.

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
  * `Sigma[A, F[_]]` is a dependent pair of `a: A` and `F[a.type]`. You can use
   `Sigma.summon` to implicitly summon a proof for a given value:
   `summon[λ[x => x <:< List[Int]]](Nil)` - is `Nil` together with a proof that
   it is a subtype of `List[Int]`.
  * `Pi[A, F[_]]` is a dependent function from `a: A` to `F[a.type]`.

## Quick Start
```scala
resolvers += Resolver.bintrayRepo("alexknvl", "maven")
libraryDependencies += "com.alexknvl"  %%  "polymorphic" % "0.5.0"
// Currently available only for Scala 2.12 and 2.13
```

```scala
import polymorphic._
import polymorphic.syntax.all._

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
baz match { case Exists(f) => f.show(f.x) } // "1"

val optToList: Option ~> List = FunctionK(_.toList)
val listToOpt = FunctionK[List, Option](_.headOption)
```

## Implementation notes
Neither `Exists` nor `Forall` box their contents. Both are essentially opaque
newtypes over `F[X]`:
```scala
type Exists[F[_]] <: Any { type T = A }
type Forall[F[_]] <: Any
```

Note that `Forall[F]` is evaluated **eagerly and only once**. If you need a lazy
version of `Forall` you might want to consider wrapping `F` into `cats.Eval` or
similar.

## License
Code is provided under the MIT license available at https://opensource.org/licenses/MIT,
as well as in the LICENSE file.
