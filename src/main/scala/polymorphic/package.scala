package object polymorphic {
    val Forall: ForallT = new ForallImpl
    type Forall[F[_]] = Forall.∀[F]
    type ∀[F[_]]      = Forall.∀[F]

    val Exists: ExistsT = new ExistsT
    type Exists[F[_]] = Exists.∃[F]
    type ∃[F[_]]      = Exists.∃[F]

    type ~>[A[_], B[_]] = cats.arrow.FunctionK[A, B]
    def fn: MkFunctionK = new MkFunctionK
}
