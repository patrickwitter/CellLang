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
}
