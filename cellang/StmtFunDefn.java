import java.util.ArrayList;


/**
 * IR Class to represent a function definition
 */
public class StmtFunDefn extends Statement {
    // Implement this class
    String funName; // This is the function Name 
    ArrayList<String> paramList = new ArrayList<String>(); // parameter list 
    ArithProgram body; // body 
    Environment<Integer> closingEnv; // closing env 

    public StmtFunDefn(String name,ArrayList<String> listParams, Exp e) {	
    //For function defs with semi colon 
	super("funDef",e);
    funName = name;
    paramList = listParams;
    body = new ArithProgram( new StmtSequence( new Statement(e)));
    
    }

    public StmtFunDefn(String name, ArrayList<String> listParams, StmtSequence bod)
    {
        //For function defs with body 
        super("funDef",bod);
        funName = name;
        paramList = listParams;
        body = new ArithProgram(bod);
    }

    public String getfunName(){
        return funName;
    }
    
    public ArithProgram getFunBody()
    {
        return body;
    }

    public Exp getExp() {
    return getSubTree(0);
    }

    // public int eval(Environment<Integer> env) throws RuntimeException {
    // closingEnv = env;
    // Closure<Integer> c = new Closure<Integer>(this, closingEnv);
    // env.putClosure(getfunName(), c);
    // return -1;
    // }
    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        // TODO Auto-generated method stub
        return v.visitStmtFunDefn(this, arg);
    }
    public String toString() {
    return String.format("%s := %s", getfunName(), getExp().toString());
    }

    

}
