import polymorphic._

class FunctionK {
    val f1: Option ~> List = fn(_.toList)
    val f2 = fn[Option, List](_.toList)
}
