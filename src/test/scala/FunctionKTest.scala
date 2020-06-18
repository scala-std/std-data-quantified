import org.scalatest.FunSuite
import std.quantified._

class FunctionKTest extends FunSuite {
    test("constructors") {
        val f1: Option ~> List = fn(_.toList)
        val f2 = fn[Option, List](_.toList)

        val f3: Option ~> List = FunctionK(_.toList)
        val f4 = FunctionK[Option, List](_.toList)
    }
}
