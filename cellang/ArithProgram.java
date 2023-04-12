public class ArithProgram extends ASTNode<StmtSequence> {

    public ArithProgram(StmtSequence s) {
	super("program", s);
    }

    public StmtSequence getSeq() {
	return getSubTree(0);
    }

    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
	return v.visitArithProgram(this, arg);
    }

    public String toString() {
	return getSeq().toString();
    }
}
