import java.util.*;

public class ToScheme implements Visitor<Void, String> {

    String result;

    public ToScheme() {
	result = "";
    }

    public String getResult() {
	return result;
    }

    public Void getDefaultState() {
	return null;
    }

    // program
    public String visitCellProgram(CellProgram p, Void arg)
	throws VisitException {
	result = (String) p.getSeq().visit(this, arg);
	return result;
    }

    // statements
    public String visitStatement(Statement stmt, Void arg)
	throws VisitException {
	return stmt.getExp().visit(this, arg);
    }

    public String visitStmtSequence(StmtSequence exp, Void arg)
	throws VisitException {
	ArrayList stmts = exp.getSeq();
	if (stmts.size() == 1)
	    return ((Statement) stmts.get(0)).visit(this,
						    arg);
	else {
	    Iterator iter = stmts.iterator();
	    String result = "(begin ";
	    Statement stmt;
	    while (iter.hasNext()) {
		stmt = (Statement) iter.next();
		result += (String) stmt.visit(this, arg) +
		    "\n";
	    }
	    result += ")";
	    return result;
	}
    }

    public String visitStmtDefinition(StmtDefinition sd, Void arg)
	throws VisitException {
	String valExp = (String) sd.getExp().visit(this,
						   arg);
	return "(define " + sd.getVar() + " " +
	    valExp + ")";
    }

    public String visitStmtFunDefn(StmtFunDefn fd, Void env)
	throws VisitException {
	//TODO to be implemented
	
	return "";
    }

    public String visitExpFunCall(ExpFunCall fc, Void env)
	throws VisitException {
	//TODO to be implemented

	return "";
    }

    // expressions
    public String visitExpAdd(ExpAdd exp, Void arg)
	throws VisitException {
	String left = exp.getExpL().visit(this, arg);
	String right = exp.getExpR().visit(this, arg);
	return "(+ " + left + " " + right + ")";
    }
    public String visitExpSub(ExpSub exp, Void arg)
	throws VisitException {
	String left = exp.getExpL().visit(this, arg);
	String right = exp.getExpR().visit(this, arg);
	return "(- " + left + " " + right + ")";
    }

    public String visitExpMul(ExpMul exp, Void arg)
	throws VisitException {
	String left = exp.getExpL().visit(this, arg);
	String right = exp.getExpR().visit(this, arg);
	return "(* " + left + " " + right + ")";
    }

    public String visitExpDiv(ExpDiv exp, Void arg)
	throws VisitException {
	String left = exp.getExpL().visit(this, arg);
	String right = exp.getExpR().visit(this, arg);
	return "(/ " + left + " " + right + ")";
    }

    public String visitExpMod(ExpMod exp, Void arg)
	throws VisitException{
	String left = exp.getExpL().visit(this, arg);
	String right = exp.getExpR().visit(this, arg);
	return "(mod " + left + " " + right + ")";
    }

    public String visitExpLit(ExpLit exp, Void arg)
	throws VisitException{
	return "" + exp.getVal();
    }

    public String visitExpVar(ExpVar exp, Void arg)
	throws VisitException {
	return exp.getVar();
    }

	@Override
	public String visitExpLogic(ExpLogic expLogic, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitExpIf(ExpIf exp, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitExpString(ExpString expString, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitList(ExpList expList, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitExpDouble(ExpDouble expDouble, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visitExpDouble'");
	}

	@Override
	public String visitExpTable(ExpTable expTable, Void arg) throws VisitException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visitExpTable'");
	}

	@Override
	public String visitExpPrint(PrintStatement expPrint, Void state) throws VisitException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visitExpPrint'");
	}

	@Override
	public String visitExpPrintln(PrintlnStatement expPrintln, Void state) throws VisitException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visitExpPrintln'");
	}

	@Override
	public String visitIfStatement(IfStatement ifStatement, Void state) throws VisitException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visitIfStatement'");
	}

}
