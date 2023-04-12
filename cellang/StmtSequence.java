import java.util.*;

/**
 * Class to represent a sequence of statements.  This allows them to be
 * treated as a compound statement, which can be regarded as a single entity
 * with its own rules for being visited.
 */
public class StmtSequence extends Statement {

    public StmtSequence() {
	super("sequence");
    }

    public StmtSequence(Statement s) {
	super("sequence", s);
    }

    public ArrayList<Exp> getSeq() {
	return getSubTrees();
    }

    public StmtSequence add(Statement s) {
	ArrayList<Exp> seq = getSubTrees();
	seq.add(s);
	return this;
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitStmtSequence(this, arg);
    }

    public String toString() {
	Iterator<Exp> iter = getSubTrees().iterator();

	String result = "";
	while (iter.hasNext()) {
	    result = result + iter.next().toString() + "\n";
	}

	return result;
    }
}

