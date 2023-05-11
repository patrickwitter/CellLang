public class CloseOutStatement extends Statement {

    public CloseOutStatement() {
        super("Close Statment");
        
    }

    public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitCloseOutStatement(this, state);
	}

    @Override
    public String toString() {
        return "OUT";
    }


}
