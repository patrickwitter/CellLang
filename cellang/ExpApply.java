import java.util.ArrayList;

public class ExpApply extends Exp {

    private ArrayList<Exp> params;
    protected ExpApply(ArrayList<Exp> p) {
        super("apply");
        params = p;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitApplyFunction(this, arg);
    }

    @Override
    public String toString() {
       return "Apply";
    }

    public ArrayList<Exp> getparams()
    {
        return params;
    }
    
}
