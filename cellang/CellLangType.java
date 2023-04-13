public abstract class CellLangType<T> {
    private T value;
	
	public CellLangType(T value)
	{
		this.value = value;
	}

	public CellLangType()
	{
		this.value = null;
	}

	public String toString()
	{
		return getValue().toString();
	}
	
	public T getValue()
	{
		return value;
	}

	/// ARITHMETIC OPERATORS 
	public CellLangType add(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType sub(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType mul(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
	
	public CellLangType div(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType mod(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	//LOGICAL OPERATORS 
	public CellLangType and(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
	
	public CellLangType or(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
	
	public CellLangType not() throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType lessThan(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
	
	public CellLangType greaterThan(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType greaterThanOrEqual(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType lessThanOrEqual(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}

	public CellLangType notEqual(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
	
	public CellLangType areEqual(CellLangType v) throws TypeException
	{
		throw new TypeException();
	}
}
