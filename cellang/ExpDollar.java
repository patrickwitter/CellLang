public class ExpDollar extends Exp {

    ExpVar var;

    public ExpDollar(String id) {
	super("var");
	var = new ExpVar( id);
    }

    public ExpVar getVarExp() {
	return var;
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitExpDollarVar(this, arg);
    }

    public String toString() {
	return var.getVar();
    }
}
