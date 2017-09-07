import cats.arrow.FunctionK

package object polymorphic {
    val Forall: ForallT = new ForallImpl
    type Forall[F[_]] = Forall.∀[F]
    type ∀[F[_]]      = Forall.∀[F]
    def ∀ : MkForall = new MkForall
    implicit def toMkForall(f: ForallT): MkForall =
        new MkForall

    val Exists: ExistsT = new ExistsT
    type Exists[F[_]] = Exists.∃[F]
    type ∃[F[_]]      = Exists.∃[F]
    def ∃ : MkExists = new MkExists

    type ~>[A[_], B[_]] = cats.arrow.FunctionK[A, B]
    def fn: MkFunctionK = new MkFunctionK
    implicit def toMkFunctionK(f: FunctionK.type): MkFunctionK =
        new MkFunctionK
}
