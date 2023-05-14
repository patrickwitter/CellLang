

public class ExpSliceTable extends Exp {

    private ExpVar tblnm;
    private String col1;
    private String col2;
    protected ExpSliceTable(ExpVar table, String col1, String col2) {
        super("Slice Table");
        tblnm = table;
        this.col1 = col1;
        this.col2 = col2;

    }

    protected ExpSliceTable(ExpVar table, Integer col1, Integer col2) {
        super("Slice Table");
        tblnm = table;
        this.col1 =  Integer.toString( col1);
        this.col2 = Integer.toString( col2);

    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpSliceTable(this, arg);
    }

    public ExpVar getTableName()
    {
        return tblnm;
    }

    public String getColumn1()
    {
        return col1;
    }

    public String getColumn2()
    {
        return col2;
    }

    @Override
    public String toString() {
        return "Slicing Table " + tblnm.var + "From Column " + col1 + "to " + col2;
    }
    
}
