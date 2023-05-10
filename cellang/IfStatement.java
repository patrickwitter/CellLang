public class IfStatement extends Statement {
    //TODO IMPLEMENENT IFSTATEMENT
    public IfStatement(Exp e) {
        super(e);
        
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
