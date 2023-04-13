import java.io.Reader;
import java.io.StringReader;
import lib3652.util.Interpreter;
import lib3652.util.ResultType;
import lib3652.util.Result;
import lib3652.util.TokenException;

public class CellLangToScheme extends AssessmentVisitor<Void, String> {
    /**
     * Create a new Arithmetic Interpreter with a default global environment.
     */ 
    public CellLangToScheme() {
	super(new ToScheme());
    }

    public Result toResult(String r) {
	return new Result(ResultType.V_STRING, r);
    }
}
