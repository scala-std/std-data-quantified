package polymorphic

import org.scalatest.FunSpec
import syntax.all._

class ExistsTest extends FunSpec {
    trait Show[A] {
        def show(a: A): String
    }
    type Showable0[X] = (X, Show[X])
    type Showable = Exists[Showable0]

    val intShow: Show[Int] = (a: Int) => a.toString

    def test1: Unit = {
        val s1: Showable = Exists.wrap[Showable0, Int]((10, intShow))

        val x = Exists.unwrap[Showable0](s1)
        x._2.show(x._1)

        // This line doesn't work in IJ.
        s1.value._2.show(s1.value._1)
    }
}