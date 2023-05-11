public class OutStatement extends Statement {

    private String WorkbookPath;
    private String SheetPath;

    public OutStatement(String wPath, String sPath) {

        super("Out Statement");

        WorkbookPath = wPath;
        SheetPath = sPath;
        
    }

    public <S, T> T visit(Visitor<S, T> v, S state) throws VisitException
	{
		return v.visitOutStatement(this, state);
	}

    @Override
    public String toString() {
        return "OUT";
    }

    public String getWorkbookPath ()
    {
        return WorkbookPath;
    }

    public String getSheetName()
    {
        return SheetPath;
    }
    
}
