public class IfStatement extends Statement {
    
    ExpLogic predicate;
    StmtSequence consequent;
    StmtSequence alternative;
    public IfStatement(ExpLogic pred, StmtSequence con) {
        super("IfStatement");
        predicate = pred;
        consequent = con;
    }

    public IfStatement(ExpLogic pred, StmtSequence con, StmtSequence alt) {
        super("IfElseStatement");
        predicate = pred;
        consequent = con;
        alternative = alt;
    }

    public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitIfStatement(this, state);
	}

    @Override
    public String toString() {
        return "println("+super.getSubTree(0).toString()+")";
    }
    
}
