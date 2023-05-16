public class CellString extends CellLangType<String>{ 
    public CellString(String value)
	{
		super(value);
	}

	@Override
	public CellLangType mul(CellLangType v) throws TypeException {
		
		if(v instanceof CellInteger)
		{
			CellInteger repeat = (CellInteger) v;
			return new CellString( this.getValue().repeat(repeat.getValue()));
		}

		throw new TypeException("Multiplication unsupported for String and  object of class: " + v.getClass().getSimpleName());
	}



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
