

public class ExpLogic extends ASTNode<ExpLogic> {
    Cmp operator;
    Exp left;
    Exp right;

    public ExpLogic(Exp l, Exp r, Cmp c) {
        super("logicExp");
        left = l;
        right = r;
        operator = c;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        
        return v.visitExpLogic(this,arg);
    }

    @Override
    public String toString() {
        
        return "";
    }
    
    
}
