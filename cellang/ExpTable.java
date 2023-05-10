import java.util.ArrayList;

public class ExpTable extends Exp {
    private ExpList columns;
    private ArrayList<ExpList> rows;
    private int numrows;
    protected ExpTable(ExpList cols) {
        super("no rows");
        columns = cols;
        
      
    }

    protected ExpTable(ExpList cols,int numrows) {
        super("num rows");
        columns = cols;
        this.numrows = numrows;
    }
    
    protected ExpTable(ExpList cols,ArrayList<ExpList>rows) {
        super("rows specified");
        columns = cols;
        // System.out.println(rows);
        this.rows = rows;
      
    }
    
    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpTable(this,arg);
    }

    ExpList getCol ()
    {
        return columns;
    }

    ArrayList<ExpList> getRows()
    {
        return rows;
    }

    int getNumRows()
    {
        return numrows;
    }

    @Override
    public String toString() {
       return "Table";
    }
    
}
