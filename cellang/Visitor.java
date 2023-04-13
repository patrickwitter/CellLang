/**
 * The generic Visitor interface for the Arithmetic parser
 * example.
 * @param <S> The type of the information needed by the visitor
 * @param <T> The type of result returned by the visitor 
 */
public interface Visitor<S, T> {

    // to facilitate generic constructors
    public S getDefaultState();

    // program
    public T visitArithProgram(ArithProgram p, S arg) throws VisitException;

    // statements
    public T visitStatement(Statement exp, S arg) throws VisitException ;
    public T visitStmtSequence(StmtSequence exp, S arg) throws VisitException ;
    public T visitStmtDefinition(StmtDefinition sd, S arg) throws VisitException;

    public T visitStmtFunDefn(StmtFunDefn fd, S arg) throws VisitException;
    public T visitExpFunCall(ExpFunCall fc, S arg) throws VisitException;

    // expressions
    public T visitExpAdd(ExpAdd exp, S arg) throws VisitException ;
    public T visitExpSub(ExpSub exp, S arg) throws VisitException;
    public T visitExpMul(ExpMul exp, S arg) throws VisitException;
    public T visitExpDiv(ExpDiv exp, S arg) throws VisitException;
    public T visitExpMod(ExpMod exp, S arg) throws VisitException;
    public T visitExpLit(ExpLit exp, S arg) throws VisitException;
    public T visitExpVar(ExpVar exp, S arg) throws VisitException;

    public T visitExpLogic(ExpLogic expLogic, S arg) throws VisitException;

    public T visitExpIf(ExpIf exp, S arg) throws VisitException;

    public T visitExpString(ExpString expString, S arg) throws VisitException;
}
