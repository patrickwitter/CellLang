
public class StmtDefinition extends Statement {

    String var;
    //Exp exp;

    public StmtDefinition(String id, Exp e) {
	super(":=", e);
	var = id;
	// exp = e;
    }

    public String getVar(){
	return var;
    }

    public Exp getExp() {
	return getSubTree(0);
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitStmtDefinition(this, arg);
    }

    public String toString() {
	return String.format("%s := %s", getVar(), getExp().toString());
    }
}
