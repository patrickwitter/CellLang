import java.util.ArrayList;

public class Statement extends Exp {

    //Exp exp;

    // public Statement() {
    // 	super();
    // }
    
    protected Statement(String name, Exp... operands) {
	super(name, operands);
    }

    protected Statement(String name, ArrayList<Exp> operandList) {
	super(name, operandList);
    }

    public Statement(Exp e) {
	//exp = e;
	super("stmt", e);
    }

    public Exp getExp() {
	return getSubTree(0);
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitStatement(this, arg);
    }

    public String toString() {
	return getExp().toString();
    }
}
