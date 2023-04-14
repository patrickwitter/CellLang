public class CellBoolean extends CellLangType<Boolean>{
    
    //TODO Add all boolean operators 
    public CellBoolean(Boolean value)
	{
		super(value);
	}

	public CellLangType and(CellLangType v) throws TypeException
	{
		if(v instanceof CellBoolean)
		{
			Boolean result = (Boolean)getValue() && (Boolean)v.getValue();
			return new CellBoolean(result);
		}
		
		throw new TypeException();
	}
	
	public CellLangType or(CellLangType v) throws TypeException
	{
		if(v instanceof CellBoolean)
		{
			Boolean result = (Boolean)getValue() || (Boolean)v.getValue();
			return new CellBoolean(result);
		}

		throw new TypeException();
	}
	
	public CellLangType not() throws TypeException
	{
		Boolean result = !(Boolean)getValue();
		return new CellBoolean(result);
	}

    public CellLangType areEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellBoolean)
		{
			Boolean result = (Boolean)getValue() == (Boolean)v.getValue();
			return new CellBoolean(result);
		}

		throw new TypeException();
	}

	@Override
	public String toString() {
		return getValue() ? "true" : "false";
	}

}
