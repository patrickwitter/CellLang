import java.util.*;

public class Evaluator implements Visitor<Environment<Double>, Double> {
    /* For this visitor, the argument passed to all visit
       methods will be the environment object that used to
       be passed to the eval method in the first style of
       implementation. */

    // allocate state here
    protected Double result;	// result of evaluation
    private Double defaultValue;
    private Class<Double> myClass;

    protected Evaluator() {
	this(Double.NaN);
    }

    public Evaluator(Double defaultVal) {
	// perform initialisations here
	this.defaultValue = defaultVal;
	myClass = Double.class;
	result = defaultValue;
    }

	// Returns a default global environment for the evaluator
    public Environment<Double> getDefaultState() {
	return Environment.makeGlobalEnv(myClass);
    }


	// Visits an ArithProgram node and evaluates its statement sequence
    public Double visitArithProgram(ArithProgram p, Environment<Double> env)
	throws VisitException {
	result = p.getSeq().visit(this, env);
	return result;
    }

	// Visits a Statement node and evaluates its expression
    public Double visitStatement(Statement s, Environment<Double> env)
	throws VisitException {
	return s.getExp().visit(this, env);
    }

	// Visits a StmtSequence node and evaluates each of its statements in order
    public Double visitStmtSequence(StmtSequence sseq, Environment<Double> env)
	throws VisitException {
	// remember that env is the environment
	Statement s;
	ArrayList seq = sseq.getSeq();
	Iterator iter = seq.iterator();
	Double result = defaultValue;
	while(iter.hasNext()) {
	    s = (Statement) iter.next();
	    result = s.visit(this, env);
	}
	// return last value evaluated
	return result;
    }

	// Visits a StmtDefinition node and evaluates its expression, then adds the variable and result to the environment
    public Double visitStmtDefinition(StmtDefinition sd,
				      Environment<Double> env)
	throws VisitException {
	Double result;
	result = sd.getExp().visit(this, env);
	env.put(sd.getVar(), result);
	return result;
    }

	// Visits a StmtFunDefn node and creates a closure of the function with the current environment, then adds the closure to the environment
    public Double visitStmtFunDefn(StmtFunDefn fd, Environment<Double> env)
	throws VisitException {
	Environment<Double> closingEnv = env;
    Closure<Double> c = new Closure<Double>(fd, closingEnv);
    env.putClosure(fd.getfunName(), c);
	return 0D;
    }

	// Visits an ExpFunCall node, evaluates the arguments and creates a new environment for the function call, then evaluates the function's body in this environment
    public Double visitExpFunCall(ExpFunCall fc, Environment<Double> env)
	throws VisitException {
	//TODO to be implemented
	//Make child environment
        // Retrieve function definition from env if not found then throw exception
        // iterate over parameter list and evaluate arguements. If plist not equal alist then throw excep
        // assign bindings to new frame and evaluate 

        Environment<Double> child = new Environment<Double>();

        Closure<Double> funDef = env.getClosure(fc.funName);

        if(funDef == null)
        {
            throw new RuntimeException("Function not defined or defined after Call");
        }

        for(int i = 0; i < fc.args.size(); i++)
        {
            double answer = fc.args.get(i).visit(this, env);
            child.put(funDef.getFunction().paramList.get(i),answer);
        }
        child.parent = funDef.getClosingEnv();
        return funDef.getFunction().body.visit(this, child);
	
    }

	// Visits an ExpAdd node and evaluates the left and right expressions, then adds the results
    public Double visitExpAdd(ExpAdd exp, Environment<Double> env)
	throws VisitException {
	Double val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1 + val2;
    }

	// Visits an ExpSub node and evaluates the left and right expressions, then sub the results
    public Double visitExpSub(ExpSub exp, Environment<Double> env)
	throws VisitException {
	Double val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1 - val2;
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then mul the results
    public Double visitExpMul(ExpMul exp, Environment<Double> env)
	throws VisitException {
	Double val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1 * val2;
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then div the results
    public Double visitExpDiv(ExpDiv exp, Environment<Double> env)
	throws VisitException {
	Double val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1 / val2;
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then mod the results
    public Double visitExpMod(ExpMod exp, Environment<Double> env)
	throws VisitException {
	Double val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1 % val2;
    }

	// This method visits an expression literal node and returns its value.
    public Double visitExpLit(ExpLit exp, Environment<Double> env)
	throws VisitException {
	return Double.valueOf(exp.getVal());
    }

	// This method visits an expression variable node and returns 
	// the value associated with the variable from the environment.
    public Double visitExpVar(ExpVar exp, Environment<Double> env)
	throws VisitException {
	return env.get(exp.getVar());
    }

	//This method visits an expression logical operator node and returns the result of evaluating the operator.
	public Double visitExpLogic(ExpLogic exp, Environment<Double> env)
	throws VisitException {
		boolean b =	exp.operator.apply( exp.left.visit(this, env), exp.right.visit(this, env));
		return b ? 1.0 : 0;
    }

	public Double visitExpIf(ExpIf exp, Environment<Double> env)
	throws VisitException {
		double res = exp.predicate.visit(this, env);
		if( res == 1.0)
		{
			return exp.consequent.visit(this, env);
		}
		else 
		{
			if(exp.alternative != null)
			{
				return exp.alternative.visit(this, env);
			}
			else 
			{
				return 0.0;
			}
		}
    }

	@Override
	public Double visitExpString(ExpString expString, Environment<Double> arg) throws VisitException {
		//BUG Should return a string 
		return 33.33;
	}
	
}
