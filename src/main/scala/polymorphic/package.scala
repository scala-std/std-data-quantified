import cats.arrow.FunctionK

package object polymorphic {
    val Forall: ForallT = new ForallImpl
    type Forall[F[_]] = Forall.∀[F]
    type ∀[F[_]]      = Forall.∀[F]
    def ∀ : MkForall = new MkForall
    //def ∀[F[_]]: MkForall1[F] =
    //    new MkForall1[F]
    implicit def toMkForall(f: ForallT): MkForall =
        new MkForall

    val Exists: ExistsT = new ExistsT
    type Exists[F[_]] = Exists.∃[F]
    type ∃[F[_]]      = Exists.∃[F]
    def ∃ : MkExists = new MkExists
    def ∃[F[_]]: MkExists1[F] = new MkExists1[F]

    type ~>[A[_], B[_]] = cats.arrow.FunctionK[A, B]
    def fn: MkFunctionK = new MkFunctionK
    implicit def toMkFunctionK(f: FunctionK.type): MkFunctionK =
        new MkFunctionK
}
