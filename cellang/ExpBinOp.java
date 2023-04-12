/**
 * Class to represent an expression that is a binary operator.
 */
public abstract class ExpBinOp extends Exp {

    protected ExpBinOp(String name, Exp exp1, Exp exp2){
	super(name, exp1, exp2);
    }

    public Exp getExpL() {
	return (Exp) getSubTree(0);
    }

    public Exp getExpR() {
	return (Exp) getSubTree(1);
    }

    public String toString() {
	return getExpL().toString() + getName() + getExpR().toString();
    }

}
