public class CellString extends CellLangType<String>{ 
    public CellString(String value)
	{
		super(value);
	}

	//TODO DO Boolean operations

	@Override
	public CellLangType notEqual(CellLangType v) throws TypeException {
		if (v instanceof CellString)
		{
			return new CellBoolean(!getValue().equals((String)v.getValue()));
		}
		
		throw new TypeException();
	}

	@Override
	public CellLangType areEqual(CellLangType v) throws TypeException {
		if (v instanceof CellString)
		{
			return new CellBoolean(getValue().equals((String)v.getValue()));
		}

		throw new TypeException();
	}

	@Override
	public String toString() {
	
		return this.getValue();
	}
}
