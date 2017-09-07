import org.scalatest.FunSuite
import polymorphic._
import polymorphic.syntax.all._

class ExistsTest extends FunSuite {
    class Foo[A](val value: A) {
        def show(a: A): String = a.toString
    }

    test("constructors") {
        val f1: ∃[Foo] = ∃(new Foo(1))
        val f2: ∃[Foo] = Exists(new Foo(1))
        val f3: ∃[Foo] = Exists.wrap(new Foo(1))
        val x1: String = f1.value.show(f1.value.value)
        val x2: String = f1 match {
            case Exists(f) => f.show(f.value)
        }
        val x3: String = {
            val x = Exists.unwrap(f1)
            x.show(x.value)
        }
    }
}