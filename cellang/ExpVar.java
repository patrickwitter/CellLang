public class ExpVar extends Exp {

    String var;

    public ExpVar(String id) {
	super("var");
	var = id;
    }

    public String getVar() {
	return var;
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitExpVar(this, arg);
    }

    public String toString() {
	return var;
    }
}
