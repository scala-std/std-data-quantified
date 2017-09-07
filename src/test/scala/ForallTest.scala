import org.scalatest.{FunSuite, Matchers}
import polymorphic._
import polymorphic.syntax.all._

class ForallTest extends FunSuite with Matchers {
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

    test("allocation-less") {
        val f1: ∀[Foo] = ∀(new Foo)
        f1.getClass should be (classOf[Foo[_]])

        val f2 = ∀[λ[X => Int]](1)
        f2.getClass should be (classOf[java.lang.Integer])

        val f3 = Forall.const(1)
        f3.getClass should be (classOf[java.lang.Integer])
    }
}
