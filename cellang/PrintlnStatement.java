public class PrintlnStatement extends Statement
{	
	public PrintlnStatement(Exp exp)
	{
		super("println", exp);
	}

	public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitExpPrintln(this, state);
	}

    @Override
    public String toString() {
        return "println("+super.getSubTree(0).toString()+")";
    }
}
