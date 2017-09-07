import org.scalatest.FunSpec
import polymorphic._
import polymorphic.syntax.all._

class ExistsTest extends FunSpec {
    class Foo[A](val value: A) {
        def show(a: A): String = a.toString
    }

    val f1: ∃[Foo] = ∃(new Foo(1))
    val f2: ∃[Foo] = Exists(new Foo(1))
    val f3: ∃[Foo] = Exists.wrap(new Foo(1))

//    def test1: Unit = {
//        val s1: Showable = Exists.wrap[Showable0, Int]((10, intShow))
//
//        val x = Exists.unwrap[Showable0](s1)
//        x._2.show(x._1)
//
//        // This line doesn't work in IJ.
//        s1.value._2.show(s1.value._1)
//    }
}