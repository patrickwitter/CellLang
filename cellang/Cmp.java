public enum Cmp {
	//FIXME THESE ERRORS ARE NOT HANDLED PROPERLY
    LT("<") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return (CellBoolean) arg1.lessThan(arg2) ;
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}
	}
    },

    LE("<=") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return (CellBoolean) arg1.lessThanOrEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    EQ("=") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return (CellBoolean) arg1.areEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    NE("!=") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  (CellBoolean) arg1.notEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    GT(">") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return (CellBoolean) arg1.greaterThan(arg2) ;
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    GE(">=") {
	public CellBoolean apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return (CellBoolean) arg1.greaterThanOrEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    };
    

    private String symbol;

    private Cmp(String sym) {
	symbol = sym;
    }

    public abstract CellBoolean apply(CellLangType arg1, CellLangType arg2);

    public String toString() {
	return symbol;
    }



}
