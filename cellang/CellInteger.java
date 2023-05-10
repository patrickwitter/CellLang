public class CellInteger extends CellLangType<Integer>{
    public CellInteger(Integer value)
	{
		super(value);
	}

	public CellLangType add(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Integer result = (Integer)getValue() + (Integer)v.getValue();
			return new CellInteger(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = Double.valueOf((Integer)getValue()) + (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType mul(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Integer result = (Integer)getValue() * (Integer)v.getValue();
			return new CellInteger(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = Double.valueOf((Integer)getValue()) * (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType div(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Double result = Double.valueOf((Integer)getValue()) / (Integer)v.getValue();
			return new CellDouble(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = Double.valueOf((Integer)getValue()) / (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType sub(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{	
			// String a = ((Integer)getValue()).toString();
			// String b = ((Integer)v.getValue()).toString();

			// System.out.print("Subtracting" + a +"-" + b);

			Integer result = (Integer)getValue() - (Integer)v.getValue();
			return new CellInteger(result);
		}

		if(v instanceof CellDouble)
		{
			Double result = Double.valueOf((Integer)getValue()) - (Double)v.getValue();
			return new CellDouble(result);
		}

		throw new TypeException();
	}

	public CellLangType mod(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{
			Integer result = (Integer)getValue() % (Integer)v.getValue();
			return new CellInteger(result);
		}

		throw new TypeException();
	}


    
	public CellLangType lessThan(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 < r2);
		}

		if(v instanceof CellDouble)
		{			
			Double r1 =  Double.valueOf((Integer)getValue());
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 < r2);
		}

		throw new TypeException();
	}
	
	public CellLangType greaterThan(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 > r2);
		}

		if(v instanceof CellDouble)
		{			
			Double r1 =  Double.valueOf((Integer)getValue());
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 > r2);
		}

		throw new TypeException();

	}

	public CellLangType greaterThanOrEqual(CellLangType v) throws TypeException
	{
				if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 >= r2);
		}

		if(v instanceof CellDouble)
		{			
			Double r1 =  Double.valueOf((Integer)getValue());
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 >= r2);
		}

		throw new TypeException();
	}

	public CellLangType lessThanOrEqual(CellLangType v) throws TypeException
	{
				if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 <= r2);
		}

		if(v instanceof CellDouble)
		{			
			Double r1 =  Double.valueOf((Integer)getValue());
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 <= r2);
		}

		throw new TypeException();
	}

	public CellLangType notEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 != r2);
		}

		if(v instanceof CellDouble)
		{			
			Double r1 =  Double.valueOf((Integer)getValue());
			Double r2 = (Double)v.getValue();
			return new CellBoolean(r1 != r2);
		}

		throw new TypeException();
	}
	
	public CellLangType areEqual(CellLangType v) throws TypeException
	{
		if(v instanceof CellInteger)
		{			
			CellInteger i = (CellInteger)v;
			
			Integer r1 = (Integer)getValue();
			Integer r2 = (Integer)i.getValue();		   			
			return new CellBoolean(r1 == r2);
		}

		if(v instanceof CellDouble)
		{			
			double r1 =  Double.valueOf((Integer)getValue());
			double r2 = (Double)v.getValue();
			return new CellBoolean(r1 == r2);
		}

		throw new TypeException();
	}

	@Override
	public String toString() {
		return getValue().toString();
	}

}
