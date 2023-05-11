

public class ExpFilterTable extends Exp{

    private ExpLogic expr;
    private ExpVar tablenm;
    protected ExpFilterTable(ExpVar table, ExpLogic expr) {
        super("Table conndition");
        tablenm = table;
        this.expr = expr;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpSelectTableCond(this, arg);
    }

    @Override
    public String toString() {
       return "Table condition";
    }

    public ExpLogic getExpLogic()
    {
        return expr;
    }

    public ExpVar getTable()
    {
        return tablenm;
    }
    
}
