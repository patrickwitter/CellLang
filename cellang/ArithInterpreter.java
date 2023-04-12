import java.io.Reader;
import java.io.StringReader;
import lib3652.util.Interpreter;
import lib3652.util.ResultType;
import lib3652.util.Result;
import lib3652.util.TokenException;

public class ArithInterpreter extends AssessmentVisitor<Environment<Double>,
							 Double> {
    /**
     * Create a new Arithmetic Interpreter with a default global environment.
     */ 
    public ArithInterpreter() {
	super(new Evaluator(0D));
    }

    public Result toResult(Double r) {
	return new Result(ResultType.V_REAL, r);
    }
}
