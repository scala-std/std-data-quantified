import org.scalatest.FunSuite
import polymorphic._
import polymorphic.syntax.all._

class ForallTest extends FunSuite {
    class Foo[A]

    test("constructors") {
        val f1: ∀[Foo] = ∀(new Foo)
        val f2: ∀[Foo] = Forall(new Foo)
        val f3: ∀[Foo] = Forall.of[Foo](new Foo)
        val f4: ∀[Foo] = Forall.of[Foo].from(new Foo)
        val f5: ∀[Foo] = Forall.from(new Prototype[Foo] {
            override def apply[X]: Foo[X] = new Foo
        })

        val f7 = ∀[Foo](new Foo)
        val f8 = Forall[Foo](new Foo)
        val f9 = Forall.of[Foo](new Foo)
        val f10 = Forall.of[Foo].from(new Foo)
        val f11 = Forall.from(new Prototype[Foo] {
            override def apply[X]: Foo[X] = new Foo
        })
    }
}
