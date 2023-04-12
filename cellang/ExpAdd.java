public class ExpAdd extends ExpBinOp {

    public ExpAdd(Exp e1, Exp e2) {
	super("+", e1, e2);
    }

    public <S, T> T visit(Visitor<S,T> v, S arg) throws VisitException {
	return v.visitExpAdd(this, arg);
    }

}

