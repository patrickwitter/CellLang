public class ImportStatement extends Statement {

    private String path;
    public ImportStatement(String p) {
        super("Import Statement");
        path = p;
    }

    public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitImportStatement(this, state);
	}

    String getPath()
    {
        return path;
    }
    
}
