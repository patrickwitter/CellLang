import java.util.ArrayList;

public class ExpList extends Exp {

    private ArrayList<Exp> content;
    protected ExpList(ArrayList<Exp> subExps) {
        
        super("list", subExps);
        content = subExps;
        
    }

   

    ArrayList<Exp> getValue()
    {
        return content;
    }
    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
       return v.visitList(this,arg);
    }

    @Override
    public String toString() {
        return "List";
    }
    
}
