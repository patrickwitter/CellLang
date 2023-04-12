

public class ExpIf extends Exp {

    ExpLogic predicate;
    Exp consequent;
    Exp alternative;
    public ExpIf(ExpLogic pred, Exp con) {
        super("IfStatement");
        predicate = pred;
        consequent = con;
    }

    public ExpIf(ExpLogic pred, Exp con, Exp alt) {
        super("IfElseStatement");
        predicate = pred;
        consequent = con;
        alternative = alt;
    }


    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        
        return v.visitExpIf(this, arg);
    }

    @Override
    public String toString() {
        
        return "";
    }
    
}
