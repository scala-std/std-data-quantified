package std.data

package object quantified {
    type Forall[F[_]] = Forall.type#Type[F]
    type ∀[F[_]]      = Forall.type#Type[F]
    def ∀      : MkForall     = new MkForall
    def ∀[F[_]]: MkForall1[F] = new MkForall1[F]
    implicit def toMkForall(f: Forall.type): MkForall =
        new MkForall

    type Exists[F[_]] = Exists.type#Type[F]
    type ∃[F[_]]      = Exists.type#Type[F]
    def ∃      : MkExists     = new MkExists
    def ∃[F[_]]: MkExists1[F] = new MkExists1[F]

    type ~>[A[_], B[_]] = FunctionK[A, B]
    def fn: MkFunctionK = new MkFunctionK
    implicit def toMkFunctionK(f: FunctionK.type): MkFunctionK =
        new MkFunctionK

    type Implies[A, B] = Implies.Type[A, B]
    type |-[A, B] = Implies.Type[A, B]
}
