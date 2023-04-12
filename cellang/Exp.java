import java.util.ArrayList;

/** A placeholder class to represent an arbitrary expression.  For
 * ArithExp, this class is really the top level ASTNode class, but by
 * including the ASTNode class as well, this code can be readily
 * generalised to larger languages that may have more sophisticated
 * forms than only algebraic expressions in it.
*/
public abstract class Exp extends ASTNode<Exp> {
    
    protected Exp(String name, Exp... subExps) {
	super(name, subExps);
    }

    protected Exp(String name, ArrayList<Exp> subExps) {
	super(name, subExps);
    }
}
