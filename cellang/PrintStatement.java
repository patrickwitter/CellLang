public class PrintStatement extends Statement
{	
	public PrintStatement(Exp exp)
	{
		super("print", exp);
	}

	public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitExpPrint(this, state);
	}

    @Override
    public String toString() {
        return "print("+super.getSubTree(0).toString()+")";
    }
}
