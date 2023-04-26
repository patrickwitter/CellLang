
public class ExpString extends Exp {

    private String val;

    public ExpString(String v) {
	super(v.toString());
	val = v;
    }

    public String getVal() {
	return val;
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitExpString(this, arg);
    }

    public String toString() {
	return val;
    }
}

