import java.io.Reader;
import java.io.StringReader;
import lib3652.util.Interpreter;
import lib3652.util.ResultType;
import lib3652.util.Result;
import lib3652.util.TokenException;

public abstract class AssessmentVisitor<S, T> extends PersistentWalker<S, T>
    implements Interpreter {

    ArithParser parser;
    boolean isVerbose = false;
    // PersistentWalker<S, T> walker;

    /**
     * Create a new Assessment Visitor from the given visitor
     */ 
    public AssessmentVisitor(Visitor<S, T> visitor) {
	super(visitor);
	// walker = new PersistentWalker<>(visitor);
    }

    public void setVerbose(boolean isVerb) {
	isVerbose = isVerb;
    }

    public abstract Result toResult(T value);

    public Result run(String input) {
	StringReader reader = new StringReader(input);
	return run(reader);
    }

    public Result run(Reader reader) {
	parser = new ArithParser(new Lexer(reader));
	ArithProgram program;
	try {
	    program = (ArithProgram) parser.parse().value;
	} catch (TokenException te) {
	    if (isVerbose)
		System.out.println(te.getMessage());
	    return new Result(ResultType.ERROR_LEXER, te.getMessage());
	} catch (Exception e) {
	    if (isVerbose)
		System.out.println(e.getMessage());
	    return new Result(ResultType.ERROR_PARSER, e.getMessage());
	}
	try {
	    T r = walk(program);
	    return toResult(r);
	} catch (VisitException e) {
	    if (isVerbose) {
		System.out.println(e.getMessage());
	    }
	    return new Result(ResultType.ERROR_RUNTIME, e.getMessage());
	}
    }
}
