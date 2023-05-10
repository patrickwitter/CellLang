public enum Cmp {
	//FIXME THESE ERRORS ARE NOT HANDLED PROPERLY
    LT("<") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  arg1.lessThan(arg2) ;
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}
	}
    },

    LE("<=") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  arg1.lessThanOrEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    EQ("=") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  arg1.areEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    NE("!=") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return   arg1.notEqual(arg2);
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    GT(">") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  arg1.greaterThan(arg2) ;
		} catch (TypeException e) {
		
			e.printStackTrace();
			return new CellBoolean(null);
		}	
	}
    },

    GE(">=") {
	public CellLangType apply(CellLangType arg1, CellLangType arg2) {
	    try {
			return  arg1.greaterThanOrEqual(arg2);
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

    public abstract CellLangType apply(CellLangType arg1, CellLangType arg2);

    public String toString() {
	return symbol;
    }



}
