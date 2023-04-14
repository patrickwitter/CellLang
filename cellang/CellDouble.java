public class CellDouble extends CellLangType<Double> {
    public CellDouble(Double value)
	{
		super(value);
	}


    public CellLangType add(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Double result = Double.valueOf((Integer)(v.getValue())) + (Double)getValue();
			return new CellDouble(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = (Double)getValue() + (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType sub(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Double result = Double.valueOf((Integer)(v.getValue())) - (Double)getValue();
			return new CellDouble(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = (Double)getValue() - (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType mul(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Double result = Double.valueOf((Integer)(v.getValue())) * (Double)getValue();
			return new CellDouble(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = (Double)getValue() * (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

    public CellLangType div(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Double result = Double.valueOf((Integer)(v.getValue())) / (Double)getValue();
			return new CellDouble(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = (Double)getValue() / (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

    public CellLangType lessThan(CellLangType v) throws TypeException
	{			
		if(v instanceof CellDouble)
		{			
			Double r1 = (Double)getValue();
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 < r2);
		}

		if(v instanceof CellInteger)
		{
			Double r1 = (Double)getValue();
			Double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 < r2);
		}

		throw new TypeException();
	}
	
	public CellLangType greaterThan(CellLangType v) throws TypeException
	{
		if(v instanceof CellDouble)
		{			
			Double r1 = (Double)getValue();
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 > r2);
		}

		if(v instanceof CellInteger)
		{
			Double r1 = (Double)getValue();
			Double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 > r2);
		}

		throw new TypeException();
	}

	public CellLangType greaterThanOrEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellDouble)
		{			
			Double r1 = (Double)getValue();
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 >= r2);
		}

		if(v instanceof CellInteger)
		{
			Double r1 = (Double)getValue();
			Double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 >= r2);
		}

		throw new TypeException();

	}

	public CellLangType lessThanOrEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellDouble)
		{			
			Double r1 = (Double)getValue();
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 <= r2);
		}

		if(v instanceof CellInteger) 
		{
			Double r1 = (Double)getValue();
			Double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 <= r2);
		}

		throw new TypeException();
	}

	public CellLangType notEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellDouble)
		{			
			Double r1 = (Double)getValue();
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 != r2);
		}

		if(v instanceof CellInteger)
		{
			Double r1 = (Double)getValue();
			Double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 != r2);
		}

		throw new TypeException();
	}
	
	public CellLangType areEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellDouble)
		{			
			double r1 = (Double)getValue();
			double r2 = (Double)v.getValue();
			return new CellBoolean(r1 == r2);
		}

		if(v instanceof CellInteger)
		{
			double r1 = (Double)getValue();
			double r2 = Double.valueOf((Integer)v.getValue());
			return new CellBoolean(r1 == r2);
		}
		
		throw new TypeException();
	}

	@Override
	public String toString() {
		return getValue().toString();
	}
}
