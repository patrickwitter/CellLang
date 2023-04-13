import java.io.Reader;
import java.io.StringReader;

public abstract class Walker<S, T>
{
	private CellParser parser;
    private Visitor<S, T>  visitor;	
	private S state;
	
    public Walker(Visitor<S, T> visitor)
	{
		this.visitor = visitor;
		this.state = visitor.getDefaultState();
    }

    public Visitor<S, T> getVisitor()
	{
		return visitor;
    }


	// NOTE(afb) :: Given a AST it walks it.
    public T walk(ASTNode<? extends ASTNode> expr) throws VisitException 
	{
		return expr.visit(visitor, state);
    }
	
    public T run(Reader reader)
	{
		parser = new CellParser(new Lexer(reader));
		CellProgram program;

		try
		{
			program = (CellProgram)parser.parse().value;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}

		try
		{
			T result = walk(program);
			return result;
		}
		catch(VisitException e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}

}
