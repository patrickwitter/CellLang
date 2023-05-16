import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.SwingConstants;



public class Evaluator implements Visitor<Environment<CellLangType>,CellLangType > {
	private BufferedReader reader;
	private ExcelTest toExcel;
	private Boolean outCalled = false;
	private Boolean closeOutCalled = false;

	public Evaluator()
	{
		reader = new BufferedReader(new InputStreamReader(System.in));
		toExcel = new ExcelTest();
	}
	
	public Environment<CellLangType> getDefaultState()
	{		
		return Environment.makeGlobalEnv(CellLangType.class);		
	}

	@Override
	public CellLangType visitCellProgram(CellProgram p, Environment<CellLangType> env) throws VisitException {
		CellLangType result = p.getSeq().visit(this, env);
	return result;
	}

	// Visits a Statement node and evaluates its expression
    public CellLangType visitStatement(Statement s, Environment<CellLangType> env)
	throws VisitException {
	return s.getExp().visit(this, env);
    }

	// Visits a StmtSequence node and evaluates each of its statements in order
	// Returns the value of the last statement 
    public CellLangType visitStmtSequence(StmtSequence sseq, Environment<CellLangType> env)
	throws VisitException {
		Statement s;
		ArrayList seq = sseq.getSeq();
		Iterator iter = seq.iterator();

		CellLangType result = null;
		
		while(iter.hasNext()) {
			s = (Statement) iter.next();
			result = s.visit(this, env);
		}
		//Close all workbooks once it is done
		toExcel.closeAll();
		return result;
    }

	// Visits a StmtDefinition node and evaluates its expression, then adds the variable and result to the environment
    public CellLangType visitStmtDefinition(StmtDefinition sd,
				      Environment<CellLangType> env)
	throws VisitException {
	CellLangType result;
	result = sd.getExp().visit(this, env);
	env.put(sd.getVar(), result);
	return new CellNil();
    }

	// Visits a StmtFunDefn node and creates a closure of the function with the current environment, then adds the closure to the environment
    public CellLangType visitStmtFunDefn(StmtFunDefn fd, Environment<CellLangType> env)
	throws VisitException {
	Environment<CellLangType> closingEnv = env;
    Closure<CellLangType> c = new Closure<CellLangType>(fd, closingEnv);
    env.putClosure(fd.getfunName(), c);
	return new CellNil();
    }

	// Visits an ExpFunCall node, evaluates the arguments and creates a new environment for the function call, then evaluates the function's body in this environment
    public CellLangType visitExpFunCall(ExpFunCall fc, Environment<CellLangType> env)
	throws VisitException {
	//TODO to be implemented
	//Make child environment
        // Retrieve function definition from env if not found then throw exception
        // iterate over parameter list and evaluate arguements. If plist not equal alist then throw excep
        // assign bindings to new frame and evaluate 

        Environment<CellLangType> child = new Environment<CellLangType>();

		try {
			Closure<CellLangType> funDef = env.getClosure(fc.funName);

			if(funDef.getFunction().paramList.size() != fc.args.size())
			{
				throw new RuntimeException("Numerical Mismatch between number of paramaters of function definition and arguements in function call");
			}
			for(int i = 0; i < fc.args.size(); i++)
			{
				CellLangType answer = fc.args.get(i).visit(this, env);
				child.put(funDef.getFunction().paramList.get(i),answer);
			}
			child.parent = funDef.getClosingEnv();
			return funDef.getFunction().body.visit(this, child);
			
		} catch (UnboundVarException e) {
			if(BuiltInFunctions.builtInFunctions.contains(fc.funName))
			{
				if(fc.funName.equals( BuiltInFunctions.apply))
				{
					return visitApplyFunction(new ExpApply(fc.args), env);
				}
			}
			System.out.println(BuiltInFunctions.builtInFunctions);
            throw new RuntimeException("Function not defined or defined after Call: "+fc.funName);
			
		}		
	
    }

	//TODO YOU HAVE TO IMPLEMENT TOEXCEL ADD FOR EACH OPERATION 
	// Visits an ExpAdd node and evaluates the left and right expressions, then adds the results
    public CellLangType visitExpAdd(ExpAdd exp, Environment<CellLangType> env)
	throws VisitException {
	CellLangType val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);

	if(outCalled)
	{
		// System.out.println("Out ADD");
		// Check if atleast one of the expressions is an ExpTable 
		

		if(val1 instanceof CellTable || val2 instanceof CellTable)
		{
			if(val1 instanceof CellTable)
			{
				toExcel.addTables((CellTable) val1 , val2);
			}

			else
			{
				toExcel.addTables((CellTable) val2 , val1);
			}
			// System.out.println("Adding Tables to Excel");
			
		}
	}

	return val1.add( val2);
    }

	// Visits an ExpSub node and evaluates the left and right expressions, then sub the results
    public CellLangType visitExpSub(ExpSub exp, Environment<CellLangType> env)
	throws VisitException {
	CellLangType val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);

	if(outCalled)
	{
		// System.out.println("Out ADD");
		// Check if atleast one of the expressions is an ExpTable 
		

		if(val1 instanceof CellTable || val2 instanceof CellTable)
		{
			if(val1 instanceof CellTable)
			{
				toExcel.subTables((CellTable) val1 , val2);
			}

			else
			{
				toExcel.subTables((CellTable) val2 , val1);
			}
			// System.out.println("Adding Tables to Excel");
			
		}
	}
	return val1.sub(val2);
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then mul the results
    public CellLangType visitExpMul(ExpMul exp, Environment<CellLangType> env)
	throws VisitException {
	CellLangType val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);

	if(outCalled)
	{
		// System.out.println("Out ADD");
		// Check if atleast one of the expressions is an ExpTable 
		

		if(val1 instanceof CellTable || val2 instanceof CellTable)
		{
			if(val1 instanceof CellTable)
			{
				toExcel.mulTables((CellTable) val1 , val2);
			}

			else
			{
				toExcel.mulTables((CellTable) val2 , val1);
			}
			// System.out.println("Adding Tables to Excel");
			
		}
	}

	return val1.mul(val2);
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then div the results
    public CellLangType visitExpDiv(ExpDiv exp, Environment<CellLangType> env)
	throws VisitException {
	CellLangType val1, val2;

	
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);


	if(outCalled)
	{
		// System.out.println("Out ADD");
		// Check if atleast one of the expressions is an ExpTable 
		

		if(val1 instanceof CellTable || val2 instanceof CellTable)
		{
			if(val1 instanceof CellTable)
			{
				toExcel.divTables((CellTable) val1 , val2);
			}

			else
			{
				toExcel.divTables((CellTable) val2 , val1);
			}
			// System.out.println("Adding Tables to Excel");
			
		}
	}
	return val1.div(val2);
    }

	// Visits an ExpMul node and evaluates the left and right expressions, then mod the results
    public CellLangType visitExpMod(ExpMod exp, Environment<CellLangType> env)
	throws VisitException {
	CellLangType val1, val2;
	val1 = exp.getExpL().visit(this, env);
	val2 = exp.getExpR().visit(this, env);
	return val1.mod(val2); 
    }

	//TODO Note: This is an integer value only   
	// This method visits an expression literal node and returns its value.
    public CellLangType visitExpLit(ExpLit exp, Environment<CellLangType> env)
	throws VisitException {
	return new CellInteger((Integer)exp.getVal());
    }

	// This method visits an expression variable node and returns 
	// the value associated with the variable from the environment.
    public CellLangType visitExpVar(ExpVar exp, Environment<CellLangType> env)
	throws VisitException {
	return env.get(exp.getVar());
    }

	//This method visits an expression logical operator node and returns the result of evaluating the operator.
	public CellLangType visitExpLogic(ExpLogic exp, Environment<CellLangType> env)
	throws VisitException {

		/// If left is not a table  and right is a table 
		/// We throw a runtime error as that doesn't make any sense 
		/// i.e 2 > tablex ???

		if ( !(exp.left instanceof ExpTable) && exp.right instanceof ExpTable )
		{
			throw new VisitException("Tabular boolean operation must have the table on the left side", null);
		}
		CellLangType b =	exp.operator.apply( exp.left.visit(this, env), exp.right.visit(this, env));
		return b;
    }

	public CellLangType visitExpIf(ExpIf exp, Environment<CellLangType> env)
	throws VisitException {
		CellLangType res = exp.predicate.visit(this, env);

		if (!(res instanceof CellTable) )
		{
			boolean istrue =  (boolean)res.areEqual(new CellBoolean(true)).getValue();
			if(istrue )
			{
				return exp.consequent.visit(this, env);
			}
			else 
			{
				if(exp.alternative != null)
				{
					return exp.alternative.visit(this, env);
				}
				else 
				{
					return new CellInteger(0);
				}
			}
		}
		else
		{
			
			CellTable r = (CellTable) res;
			CellList rows =  r.getRows();
			CellBoolean True = new CellBoolean(true);
			CellList newRows = new CellList();

			for (int rw = 0; rw < rows.getValue().size(); rw++) {
				
				CellList row = (CellList) rows.getValue().get(rw);
			
				List<CellLangType> rowValues = row.getValue();
				
				CellList newRow = new CellList();

				for (int rwEntry = 0; rwEntry < rowValues.size(); rwEntry++) {
								
					CellLangType rowEntry = rowValues.get(rwEntry);

					if((Boolean)rowEntry.areEqual(True).getValue())
					{
						newRow.add(exp.consequent.visit(this, env));
					}
					else 
					{
						if(exp.alternative != null)
						{
							newRow.add( exp.alternative.visit(this, env));
						}
						else 
						{
							newRow.add( new CellInteger(0));
						}
					}
								
				}

				newRows.add(newRow);

			}
			return newRows;
		}
		
    }

	@Override
	public CellLangType visitExpString(ExpString expString, Environment<CellLangType> arg) throws VisitException {
		return new CellString( expString.getVal() );
	}

	@Override
	public CellLangType visitList(ExpList expList, Environment<CellLangType> arg) throws VisitException {
		
		CellList x =  new CellList();
		for (Exp i : expList.getValue())
		{
			x.add(i.visit(this, arg));
		}

		return x;
	}

	@Override
	public CellLangType visitExpDouble(ExpDouble expDouble, Environment<CellLangType> arg) throws VisitException {
		return new CellDouble((Double) expDouble.getVal());
	}

	@Override
	public CellLangType visitExpTable(ExpTable expTable, Environment<CellLangType> arg) throws VisitException {
		CellList rows = new CellList();
		CellList columns = new CellList();
		CellTable result;
		for (Exp c: expTable.getCol().getValue()) {
			CellLangType colEntry = c.visit(this, arg);

		    columns.add(new CellString(colEntry.getValue().toString()));	
			
		}

		switch(expTable.getName())
		{
			case("no rows"):
							 result = new  CellTable(columns);
							 break;
			case("num rows"):
							 result = new CellTable(columns, expTable.getNumRows());
							 break;
			case("rows specified"):
								// System.out.println("ROW");
								// System.out.println(expTable.getRows());
								
									
								
							 for (ExpList rowEn: expTable.getRows()) {
								// System.out.println("ROW Entry");
								// System.out.println(rowEn.visit(this, arg));
								CellList rowEntry = new CellList();
								 for(Exp e: rowEn.getValue())
								 {
									rowEntry.add(e.visit(this, arg));
								 }
								 rows.add(rowEntry);
							}
							 result = new CellTable(columns, rows);
							 break;
			default:
						throw new VisitException("Illegal initialization of Table");	

			
		}

		if(outCalled)
		{
			toExcel.CreateStaticTable(result);
		}
		return result;

	}

	@Override
	public CellLangType visitExpPrint(PrintStatement expPrint, Environment<CellLangType> state) throws VisitException {
		CellLangType r = expPrint.getSubTree(0).visit(this, state);
		System.out.print(r.toString());
		return new CellNil();
	}

	@Override
	public CellLangType visitExpPrintln(PrintlnStatement expPrintln, Environment<CellLangType> state) throws VisitException {
		CellLangType r = expPrintln.getSubTree(0).visit(this, state);
		System.out.println(r.toString());
		return new CellNil();
	}

	//TODO IMPLEMENT
	@Override
	public CellLangType visitIfStatement(IfStatement ifStatement, Environment<CellLangType> state)
			throws VisitException {
		return new CellInteger(999);
	}

	
	@Override
	public CellLangType visitExpSliceTable(ExpSliceTable expSliceTable, Environment<CellLangType> arg)
			throws VisitException {
		
				CellLangType t = expSliceTable.getTableName().visit(this, arg);

				if(t instanceof CellTable)
				{
					CellTable table = (CellTable)t;

					try {
						return table.slice(expSliceTable.getColumn1(), expSliceTable.getColumn2());
						
					} catch (TypeException e) {
						throw new VisitException(e.getMessage());
					}
				}
				else if(t instanceof CellList)
				{	
					String regEx = "-?\\d+(\\.\\d+)?";
					

					if(expSliceTable.getColumn1().matches(regEx) && expSliceTable.getColumn2().matches(regEx))
					{
						Integer start = Integer.parseInt(expSliceTable.getColumn1());
						Integer end = 	Integer.parseInt(expSliceTable.getColumn2());

						return ((CellList) t).slice(start, end);
					}
						
					else
					{
						throw new VisitException("start or end index not an Integer for slicing CellList");
					}
						

				}
				else {
					throw new VisitException("Slicing Operator only works for CellTables and CellLists");
				}
	}

	@Override
	public CellLangType visitExpSelectTableCol(ExpSelectTableCol expSelectTableCol, Environment<CellLangType> arg)
			throws VisitException {

				CellLangType t = expSelectTableCol.getTableName().visit(this, arg);

				if(t instanceof CellTable)
				{
					CellTable table = (CellTable)t;

					try {
						return table.getColumn(expSelectTableCol.getColumn1());
						
					} catch (TypeException e) {
						throw new VisitException(e.getMessage());
					}
				}
				else {
					throw new VisitException("Slicing Operator only works for tables");
				}
		
	}

	//TODO Implement
	@Override
	public CellLangType visitExpSelectTableCond(ExpFilterTable expSelectTableCond, Environment<CellLangType> arg)
			throws VisitException {

		//Check if the left side of the logical expression is a variable representing the column name 
		
		if (expSelectTableCond.getExpLogic().left instanceof ExpVar)
		{
			// Check if the table variable name coresponds to a table

			if(expSelectTableCond.getTable().visit(this, arg) instanceof CellTable)
			{

				// Get the column from the table
				ExpSelectTableCol getcol = new ExpSelectTableCol(expSelectTableCond.getTable(), ((ExpVar)expSelectTableCond.getExpLogic().left).var); 
				CellTable column = (CellTable)getcol.visit(this, arg);

				// Apply the condition on the column 
				CellTable subTable = (CellTable)expSelectTableCond.getExpLogic().operator.apply(column, expSelectTableCond.getExpLogic().right.visit(this, arg));
				
				
				// Return table that has
				CellTable original =  (CellTable)expSelectTableCond.getTable().visit(this, arg);
				
				CellTable x = original.filterTable(subTable);
				//TODO TEST IT 
				if(outCalled)
				{
					toExcel.CreateStaticTable(x);
				}
				
				return x;
			}
			else 
			{
				throw new VisitException("Conditional Slicing can only be called on table names");
			}
		}
		else {
			throw new VisitException("Left side of condition must be a column name");
		}
	
	}

	@Override
	public CellLangType visitOutStatement(OutStatement outStatement, Environment<CellLangType> state)
			throws VisitException {
				if(outCalled)
				{
					throw new VisitException("You must put a closeOut statment before writing out again");
				}
				outCalled = true;
				toExcel.createWorkbook( outStatement.getWorkbookPath(), outStatement.getSheetName());
				return new CellNil();
	}

	@Override
	public CellLangType visitCloseOutStatement(CloseOutStatement closeOutStatement, Environment<CellLangType> state) throws VisitException {
		if(outCalled && !closeOutCalled)
		{
			outCalled = false;
			closeOutCalled = true;
		}
		else if (closeOutCalled)
		{
			throw new VisitException("'closeOut' called previously.You must put an 'out' statment before writing closeOut again");
		}
		else
		{
			throw new VisitException("You must put an 'out' statment before writing closeOut");
		}
		return new CellNil();
	}

	@Override
	public CellLangType visitImportStatement(ImportStatement importStatement, Environment<CellLangType> state)
			throws VisitException {
				
				List<Pair<String, CellTable>> result = toExcel.readTablesFromMappingsFile(importStatement.getPath());
				for (Pair<String,CellTable> pair : result) {
					state.put(pair.getKey(), pair.getValue());
					// toExcel.CreateStaticTable(pair.getValue());
				}
				return new CellNil();
	}

	@Override
	public CellLangType visitApplyFunction(ExpApply apply, Environment<CellLangType> state) throws VisitException {
		
		if(apply.getparams().size() == 3)
		{
			if(apply.getparams().get(0) instanceof ExpVar){
				String rowOrCol = ((ExpVar) apply.getparams().get(0)).getVar();
				
				if(rowOrCol.equals("row") || rowOrCol.equals( "col"))
				{
					if (apply.getparams().get(1) instanceof ExpVar)
					{
						String fun = ((ExpVar) apply.getparams().get(1)).getVar();
						// System.out.println(fun);
						if(fun.equals( "sum"))
						{
							if(apply.getparams().get(2) instanceof ExpVar)
							{
								CellLangType t =  ((ExpVar) apply.getparams().get(2)).visit(this, state);

								if (t instanceof CellTable)
								{
									return ((CellTable) t).applySum();
								}
							}
							throw new VisitException("Final arguement should be a CellTable ");
						}
					}

					throw new VisitException("Second arguement should be a function: Built in or otherwise.");
				}
			}
			
			
				throw new VisitException("First arguement should be 'row or col'.");
			
		}
		
		throw new VisitException("Incorrect Amount of parameters. Expecting 3");
	}

	@Override
	public CellLangType visitExpMethodCall(ExpMethodCall expMethodCall, Environment<CellLangType> arg) throws VisitException {
		
		CellLangType object = expMethodCall.getObjectNm().visit(this, arg);

		if(object instanceof CellTable)
		{
			return visitCellTableMethods(expMethodCall,(CellTable)object,arg);
		}

		throw new VisitException("Method Call not supported on Object");
	}

	private CellLangType visitCellTableMethods(ExpMethodCall expMethodCall, CellTable object,Environment<CellLangType> arg) throws VisitException {
		if(BuiltInFunctions.builtInTableFunctions.contains(expMethodCall.getMethodNm()))
		{
			String methodNm = expMethodCall.getMethodNm();

			if(methodNm.equals(BuiltInFunctions.mutate))
			{
				ArrayList<Exp> param = expMethodCall.getParams();

				if(param.size() == 2)
				{
					int colNm = 0;

					if(param.get(colNm) instanceof ExpVar)
					{
						String columnNm = ((ExpVar)param.get(colNm)).getVar();

						int row = 1;

						CellLangType rowE = param.get(row).visit(this,arg);

						if(rowE instanceof CellList)
						{
							CellList rows = (CellList)rowE;

							return object.mutate(columnNm, rows.getValue());

							

						}

						throw new VisitException("Expecting second arguement to be a List");

					}
					
					throw new VisitException("Expecting first arguement to be a Column Name");
				}

				throw new VisitException("Incorrect number of paramters for method "+BuiltInFunctions.mutate+" Expecting 3");

			}
		}
		throw new VisitException("Method does not exist on CellTable");
	}

	
	
	
}
