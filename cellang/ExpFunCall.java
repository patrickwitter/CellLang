import java.util.ArrayList;
//TODO implement visit
/**
 * IR Class to represent a function call
 */
public class ExpFunCall extends Exp {
    
   ArrayList<Exp> args;
   String funName; 

    public ExpFunCall(String name, ArrayList<Exp> arguements) {	
	super("call",arguements);
    funName = name;
    args = arguements;
    }

    String getfunName()
    {
        return funName;
    }

    public Exp getExp() {
        return getSubTree(0);
        }
    public String toString() {
        return String.format("%s := %s", getfunName(), getExp().toString());
        }

    // @Override
    // public int eval(Environment<Integer> env) throws RuntimeException {
    //     //Make child environment
    //     // Retrieve function definition from env if not found then throw exception
    //     // iterate over parameter list and evaluate arguements. If plist not equal alist then throw excep
    //     // assign bindings to new frame and evaluate 

    //     Environment<Integer> child = new Environment<Integer>();

    //     StmtFunDefn funDef = env.getClosure(funName);

    //     if(funDef == null)
    //     {
    //         throw new RuntimeException("Function not defined or defined after Call");
    //     }

    //     for(int i = 0; i < args.size(); i++)
    //     {
    //         int answer = args.get(i).eval(env);
    //         child.put(funDef.paramList.get(i), answer);
    //     }
    //     child.parent = funDef.closingEnv;
    //     return funDef.getFunBody().eval(child);
    // }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        // TODO Auto-generated method stub
        return v.visitExpFunCall(this, arg);
    }

}
