public enum Cmp {
    LT("<") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 < arg2;
	}
    },

    LE("<=") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 <= arg2;	// FIXME
	}
    },

    EQ("=") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 == arg2;	// FIXME
	}
    },

    NE("!=") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 != arg2;	// FIXME
	}
    },

    GT(">") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 > arg2 ;	// FIXME
	}
    },

    GE(">=") {
	public boolean apply(double arg1, double arg2) {
	    return arg1 >= arg2;	// FIXME
	}
    }
    ;

    private String symbol;

    private Cmp(String sym) {
	symbol = sym;
    }

    public abstract boolean apply(double arg1, double arg2);

    public String toString() {
	return symbol;
    }
}
