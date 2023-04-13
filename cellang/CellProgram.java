public class CellProgram extends ASTNode<StmtSequence>
{
	
    public CellProgram(StmtSequence seq)
	{
		super("program", seq);
    }

    public StmtSequence getSeq()
	{
		return getSubTree(0);
    }

    public <S, T>  T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitCellProgram(this, state);
    }

    @Override
    public String toString() {
        return "Program Ended";
    }

}
