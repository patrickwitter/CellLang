import java.io.Reader;
import java.io.StringReader;
import lib3652.util.Interpreter;
import lib3652.util.ResultType;
import lib3652.util.Result;
import lib3652.util.TokenException;

public class CellInterpreter extends AssessmentVisitor<Environment<CellLangType>, CellLangType>
{
	public CellInterpreter()
	{
		super(new Evaluator());
	}

	@Override
	public Result toResult(CellLangType value) {
		// TODO Auto-generated method stub
		return new Result(ResultType.V_REAL, value);
	}
}
