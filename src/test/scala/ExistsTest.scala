import org.scalatest.{FunSuite, Matchers}
import std.quantified._
import std.quantified.syntax.all._

import scala.reflect.runtime.universe._

class ExistsTest extends FunSuite with Matchers {
    class Foo[A](val value: A) {
        def show(a: A): String = a.toString
    }

    def tt[A](a: A)(implicit A: TypeTag[A]): TypeTag[A] = A

    test("constructors") {
        val f1: ∃[Foo] = ∃(new Foo(1))
        val f2: ∃[Foo] = Exists(new Foo(1))
        val f3: ∃[Foo] = Exists.wrap(new Foo(1))

        val x1 = f1.value.show(f1.value.value)
        x1 should be ("1")
        tt(x1) should be (typeTag[String])

        val x2 = f1 match {
            case Exists(f) => f.show(f.value)
        }
        x2 should be ("1")
        tt(x2) should be (typeTag[String])

        val x3 = {
            val x = Exists.unwrap(f1)
            x.show(x.value)
        }
        x3 should be ("1")
        tt(x3) should be (typeTag[String])
    }

    test("allocation-less") {
        val f1: ∃[Foo] = ∃(new Foo(1))
        f1.getClass should be (classOf[Foo[_]])

        val f2: ∃[λ[x => x]] = ∃[λ[x => x]](1)
        f2.getClass should be (classOf[java.lang.Integer])

        val f3 = ∃[λ[X => Int]](1)
        f3.getClass should be (classOf[java.lang.Integer])
    }

    test("arrays") {
        val f1: ∃[Array] = ∃(Array[Int](1))
        f1.getClass should be (classOf[Array[Int]])
        val a = f1.value(0)
        a.getClass should be (classOf[java.lang.Integer])

        Array(∃[Option](Some(1)))

        type Id[x] = x
        val f2: Array[∃[Id]] = Array(∃[Id](1), ∃[Id]("str"))
        f2(0) should be (1)
        f2(1) should be ("str")
    }
}