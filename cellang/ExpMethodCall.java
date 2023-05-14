import java.util.ArrayList;

public class ExpMethodCall extends Exp {

    private ExpVar name;
    private ArrayList<Exp> params;
    private String methodNm;

    protected ExpMethodCall(String name, String method, ArrayList<Exp> p) {
        super("Mehod");
        this.name = new ExpVar(name);
        params = p;
        this.methodNm = method;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpMethodCall(this,arg);
    }

    @Override
    public String toString() {
        return "Method Call '"+methodNm+"' on "+name; 
    }

    public ExpVar getObjectNm()
    {
        return name;
    }

    public String getMethodNm()
    {
        return methodNm;
    }

    public ArrayList<Exp> getParams()
    {
        return params;
    }
    
}
