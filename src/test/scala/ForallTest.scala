import polymorphic._
import polymorphic.syntax.all._

class ForallTest {
    class Foo[A]

    val f1: ∀[Foo] = ∀(new Foo)
    val f2: ∀[Foo] = Forall(new Foo)
    val f3: ∀[Foo] = Forall.of[Foo](new Foo)
    val f4: ∀[Foo] = Forall.of[Foo].from(new Foo)
    val f5: ∀[Foo] = Forall.from(new Prototype[Foo] {
        override def apply[X]: Foo[X] = new Foo
    })
}
