import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to represent an arbitrary AST Node.  Every language form would have
 * an instance of this class to represent it.
 * @param E represents that specific class type of the sub-forms 
 */ 
public abstract class ASTNode<E extends ASTNode> {

    private String name;    // the type name of this node (e.g. "+", "if")
    private ArrayList<E> subTrees; // the sub expressions

    /**
     * Create a new node of the specified type with subtrees rooted at the
     * given argument nodes.
     */
    protected ASTNode(String name, E... subExps) {
	this.name = name;
	subTrees = new ArrayList<E>();
	for (E c : subExps) {
	    subTrees.add(c);
	}
    }

    /**
     * Create a new node of the specified type with subtrees rooted at the
     * given list of nodes.
     */
    protected ASTNode(String name, ArrayList<E> subExps) {
	this.name = name;
	this.subTrees = subExps;
    }

    /**
     * Return the sub trees of this node as an array list
     */
    public ArrayList<E> getSubTrees() {
	return subTrees;
    }

    /**
     * @param index The index of the requested subtree
     * @return the subtree at the given index
     */
    public E getSubTree(int index) {
	return subTrees.get(index);
    }

    /**
     * Return the name for this type of node.  Typically, this will be the
     * human readable printed representation for an operator or the
     * keyword that makes it clear what type of expression / statement this
     * node represents.
     */
    public String getName() {
	return name;
    }

    /** Method to support Visitor design pattern. A call to visit on
     * the ASTNode results in a specific method call in the provided
     * visitor.  This way, each subclass of ASTNode will make a direct
     * call to its associated method in the visitor.
     */
    public abstract <S, T> T visit(Visitor<S, T> v, S arg)
	throws VisitException ;

    /** Return a string representation of this node and its subtree.
     * The returned string should bear some resemblance to the user's
     * input that would have given rise to this subtree being
     * constructed in the first place, since this method is likely to
     * be called during debugging, and the returned value is likely to
     * be shown to the end user.
     */
    public abstract String toString();
}
