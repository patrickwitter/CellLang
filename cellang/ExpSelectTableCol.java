

public class ExpSelectTableCol extends Exp {

    private ExpVar tblnm;
    private String col1;

    protected ExpSelectTableCol(ExpVar tbl, String col) {
        super("Selecting Column");
        tblnm = tbl;
        col1 = col;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpSelectTableCol(this, arg);
    }

    @Override
    public String toString() {
       return "Select Column " + col1 + "from " + this.tblnm.var; 
    }

    public ExpVar getTableName()
    {
        return tblnm;
    }

    public String getColumn1()
    {
        return col1;
    }
    
}
