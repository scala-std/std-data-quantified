import org.scalatest.FunSuite
import polymorphic._
import polymorphic.syntax.all._

class InstanceTest extends FunSuite {
    trait Show[A] {
        def show(a: A): String
    }

    test("implicit resolution") {
        implicit val intShow: Show[Int] = (a: Int) => a.toString

        def foo(a: Instance[Show]): String =
            a.second.show(a.first)
        foo(10)

        def bar(a: Instance[Show]*): String =
            a.map(x => x.second.show(x.first)).mkString(", ")
        bar(1, 2, 3)

        val f1 = Instance[Show](1)
    }
}
