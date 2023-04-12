
/**
 * Runtime representation for a procedure object (under static scoping)
 * @param T the type of values stored in the closing environment.
 */
public class Closure<T> {
    private StmtFunDefn function;
    private Environment<T> closingEnv;

    public Closure(StmtFunDefn fun, Environment<T> env) {
	function = fun;
	closingEnv = env;
    }

    public StmtFunDefn getFunction() {
	return function;
    }

    public Environment<T> getClosingEnv() {
	return closingEnv;
    }
}
