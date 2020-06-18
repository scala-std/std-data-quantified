import org.scalatest.{FunSuite, Matchers}
import std.data.quantified._
import std.data.quantified.syntax.all._

class InstanceTest extends FunSuite with Matchers {
    trait Show[A] {
        def show(a: A): String
    }

    implicit val intShow: Show[Int] = new Show[Int] {
        def show(a: Int): String = a.toString
    }

    test("implicit resolution") {
        def foo(a: Instance[Show]): String =
            a.second.show(a.first)
        foo(10) should be ("10")

        def bar(a: Instance[Show]*): String =
            a.map(x => x.second.show(x.first)).mkString(", ")
        bar(1, 2, 3) should be ("1, 2, 3")

        val f1 = Instance[Show](1)
    }

    test("deconstruction") {
        def foo(a: Instance[Show]): String = a match {
            case Instance(i, s) => s.show(i)
        }

        foo(1) should be ("1")
    }
}
