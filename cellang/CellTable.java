import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CellTable extends CellLangType<List<CellLangType>>{

    //TODO depending .... return 0 or empty string
    private CellList columns;

    // Specifically a list of lists
    private CellList rows;

    private static int tableId = 0;
    
    private int uniqueTableId;

    private Set<String> tableTagsSeen = new HashSet<>();

    private String tableTag;



    public void setTableTag(String tag) throws RuntimeException
    {
        if (tableTagsSeen.contains(tag))
        {
            throw new RuntimeException("Tags must be unique");
        }
        else{
            tableTag = tag;
            tableTagsSeen.add(tag);
        }

    }

    /// All CellTables must have a list of columns
    /// By default one empty row is created 
    /// E.G. table(["COL1,COL2"])
    public CellTable(CellList columns) {
        super(new ArrayList<CellLangType>() {{
            add(columns);
            add(new CellList());
        }});

        this.columns = columns;
        rows = new CellList();
       uniqueTableId =  tableId++;
        /// TABLE OF THE FORM [ ["Col1", "Col2"], [] ]
    }

 
    /// Creates a table with number of rows 
    public CellTable(CellList columns, int numrows) throws TypeException {
       
        super(new ArrayList<CellLangType>() {{
            CellList r = new CellList();
            add(columns);
            for (int i = 0; i < numrows; i++) {
                //TODO Ineffi
                r.add(new CellList());
                add(new CellList());
            }
            
        }});

        this.columns = columns;
        this.rows = new CellList( super.getValue().subList(1, super.getValue().size()));
        uniqueTableId =  tableId++;
         /// []*0* - The 0th index
         /// TABLE OF THE FORM [ ["Col1", "Col2"], []*0*,.......[]*numrows-1* ]
    }


    /// Creates a table with the rows specified 
    /// The table is a list of lists 
    /// with the first element being the column and the rest the rows
    public CellTable(CellList columns, CellList rows) {
        /// rows of the form 
        /// [[1,2,3], [4,5,6]]
        super(new ArrayList<CellLangType>() {{
            add(columns);
            for (int i = 0; i < rows.getValue().size(); i++) {
                add(rows.getValue().get(i));
            }
        }});
        this.columns = columns;
        this.rows = new CellList( super.getValue().subList(1, super.getValue().size()));
        uniqueTableId =  tableId++;
        /// TABLE OF THE FORM [ ["Col1", "Col2"], [row1],[row2] ]
    }

    @Override
    public CellTable add(CellLangType v) throws TypeException {
        
        /// If it is another table we add each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

            //    CellList tableARow = (CellList)tableA.getRows().getValue().get(i);
            //    CellList tableBRow = (CellList)tableB.getRows().getValue().get(i);

                
                CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {
                    /// Within EXCEL EMPTY CELLS ARE TREATED AS 0 IN ARITHMETIC OPERATIONS 
                    //a.add( tableARow.getValue().get(j).add(tableBRow.getValue().get(j)));
                    CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                    CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                    a.add(tableAElement.add(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

        if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Add each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.add(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }

        throw new TypeException();

    }

    @Override
    public CellTable sub(CellLangType v) throws TypeException {
        /// If it is another table we add each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 SUB first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row subtractions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row subtractions we do
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row subtractions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

            //    CellList tableARow = (CellList)tableA.getRows().getValue().get(i);
            //    CellList tableBRow = (CellList)tableB.getRows().getValue().get(i);
               CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
               CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();

               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {
                    CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                    CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                    a.add(tableAElement.sub(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

        if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Substract each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.sub(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }

        throw new TypeException();
    }

    @Override
    public CellTable mul(CellLangType v) throws TypeException {
        /// If it is another table we add each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB = (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

               //CellList tableARow = (CellList)tableA.getRows().getValue().get(i);
               //CellList tableBRow = (CellList)tableB.getRows().getValue().get(i);

               CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
               CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                a.add(tableAElement.mul(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

        if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Add each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.mul(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }

        throw new TypeException();
    }

    @Override
    public CellTable div(CellLangType v) throws TypeException {
        /// If it is another table we add each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                a.add(tableAElement.div(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

        if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Add each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.div(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }

        throw new TypeException();
    }

    /// In boolean types, In Excel empty cells are evaluated as 0 . 
    public CellTable lessThan(CellLangType v) throws TypeException
	{	
        
        /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                a.add(tableAElement.lessThan(tableBElement));
               
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.lessThan(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }

		throw new TypeException();
	}
	
	public CellTable greaterThan(CellLangType v) throws TypeException
	{   

                /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

         
                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                    CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                    a.add(tableAElement.greaterThan(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }
		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.greaterThan(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }
        throw new TypeException();
	}

	public CellTable greaterThanOrEqual(CellLangType v) throws TypeException
	{   
                /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

         
                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                a.add(tableAElement.greaterThanOrEqual(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.greaterThanOrEqual(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }
        throw new TypeException();

	}

	public CellTable lessThanOrEqual(CellLangType v) throws TypeException
	{   
                /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

         
                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                    CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                    a.add(tableAElement.lessThanOrEqual(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.lessThanOrEqual(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }
        throw new TypeException();
	}

	public CellTable notEqual(CellLangType v) throws TypeException
	{
                /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

         
                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                    CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                    a.add(tableAElement.notEqual(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.notEqual(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }
        throw new TypeException();
	}
	
	public CellTable areEqual(CellLangType v) throws TypeException
	{
        /// If it is another table we compare each element of the table 
        /// If the tables are not the same dimensions we use the Excel Rules 
        /// IF  an "empty cell" is an added to an integer 0 will be the value 
        /// IF an "empty cell" is added to a string the empty string will be the value
        
        if(v instanceof CellTable)
        {
            CellList newRow = new CellList();
            CellTable tableA = this;
            CellTable tableB= (CellTable) v;
            // List<CellLangType> tableB = y.getValue();

            /// TABLES ARE OF THE FORM 
            /*
             *  [ ["Col1","Col2"],[row1],[row2]]
             * 
             */
            /// We add each element of each row:  first row ; first element of T1 PLUS first row ; first element of T2
            /// and so on
            
            /// Check the number of columns in each table
            ///  The table with the greater column will be the upper bound on how many inner row additions we do 
            /// An inner row addition is as follows:
            /// [1,2,3] [1,2] - The maxcol in the first list will dictate how many inner row addition additions we 
            int maxcol =  Math.max(tableA.getColumns().getValue().size(), tableB.getColumns().getValue().size());

            /// Check the number of rows in each table
            ///  The table with the greater column will be the upper bound on how many outer  row additions we do 
            /// An outer row addition is as follows:

            /// [ ["1"], [1] , [1] , [1] ]: T1
            /// [ ["1"], [1] , [1] ]: T2 - The maxrow in the first list will dictate how many outer row addition additions we do
            int maxrows = Math.max(tableA.getRows().getValue().size(), tableB.getRows().getValue().size());

            //TODO Account for different dimensions in tables 
            for (int i = 0; i < maxrows; i++)
            {   
                /*
                 * CellTable.getRows().getValue()
                 * 
                 * Returns a List of List as per the defintion in the constructor
                 * 
                 */

                 CellList tableARow = i < tableA.getRows().getValue().size() ? (CellList) tableA.getRows().getValue().get(i) : new CellList();
                 CellList tableBRow = i < tableB.getRows().getValue().size() ? (CellList) tableB.getRows().getValue().get(i) : new CellList();
               
               CellList a = new CellList();
               for (int j = 0; j < maxcol; j++)
               {

         
                CellLangType tableAElement = j < tableARow.getValue().size() ? tableARow.getValue().get(j) : new CellInteger(0);
                CellLangType tableBElement = j < tableBRow.getValue().size() ? tableBRow.getValue().get(j) : new CellInteger(0);
                a.add(tableAElement.areEqual(tableBElement));
               }

               newRow.add(a);
            }


            return new CellTable(this.columns, newRow);
           
        }

		if (v instanceof CellInteger || v instanceof CellDouble)
        {
            /// Compare each element of the table  to the constant

            /// This represents the new collection of rows in the table
            CellList newRows = new CellList();

            // This is a list of lists per the constructor definition 
            List<CellLangType> x = getRows().getValue();

            
            for (int i = 0; i < getRows().getValue().size(); i++)
            {
                CellList row = (CellList)x.get(i);

                /// A singular row 
                CellList newRow = new CellList();

                for (CellLangType element : row.getValue()) {
                    
                    newRow.add(element.areEqual(v));
                }

                newRows.add(newRow);
            }

            return new CellTable(columns, newRows);
        }
        throw new TypeException();
	}


    public CellList getColumns()
    {
        return columns;
    }

    public CellList getRows()
    {
        return rows;
    }

    public void addColumn(CellList c)
    {   
        
            columns.addAll(c);
        
    }

    public void addRow(CellList r)
    {   
        rows.add(r);
    }

    public CellTable slice(String colName1, String colName2) throws TypeException {
        int col1Index = -1;
        int col2Index = -1;

        // Find the indices of the specified column names
        for (int i = 0; i < columns.getValue().size(); i++) {
            String colName = ((CellString) columns.getValue().get(i)).getValue();
            if (colName.equals(colName1)) {
                col1Index = i;
            }
            if (colName.equals(colName2)) {
                col2Index = i;
            }
        }

        // Check if the specified column names are found
        if (col1Index == -1 || col2Index == -1) {
            throw new TypeException("One or both of the specified column names not found.");
        }

        // Check if the specified column names are in the correct order
        if (col1Index > col2Index) {
            throw new TypeException("The first column name should come before the second column name in the table.");
        }

        // Create a new list of columns for the sliced table
        CellList slicedColumns = new CellList(columns.getValue().subList(col1Index, col2Index + 1));

        // Create a new list of rows for the sliced table
        CellList slicedRows = new CellList();
        for (CellLangType row : rows.getValue()) {
            CellList slicedRow = new CellList(((CellList) row).getValue().subList(col1Index, col2Index + 1));
            slicedRows.add(slicedRow);
        }

        return new CellTable(slicedColumns, slicedRows);
    }

    public CellTable getColumn(String colName) throws TypeException {
        int colIndex = -1;

        // Find the index of the specified column name
        for (int i = 0; i < columns.getValue().size(); i++) {
            String currentColName = ((CellString) columns.getValue().get(i)).getValue();
            if (currentColName.equals(colName)) {
                colIndex = i;
                break;
            }
        }

        // Check if the specified column name is found
        if (colIndex == -1) {
            throw new IllegalArgumentException("The specified column name not found.");
        }

        // Create a new list for the column values
        CellList columnValues = new CellList();

        // Add the column name as the first element
        columnValues.add(columns.getValue().get(colIndex));

        // Add the values from the specified column
        for (CellLangType row : rows.getValue()) {
            ArrayList<CellLangType> r = new ArrayList<CellLangType>();
            r.add(((CellList) row).getValue().get(colIndex));
            CellList rowE = new CellList(r );
            columnValues.add(rowE);
        }
        ArrayList<CellLangType> c = new ArrayList<CellLangType>();

        c.add(columnValues.getValue().get(0));
        CellList column = new CellList(c );
        CellList row = new CellList(new CellList(columnValues.getValue().subList(1, columnValues.getValue().size())).getValue());
        // System.out.println("FROM SELECT COLOUMN ROW");
        // System.out.println(row);
        return new CellTable(column,row);
    }


    @Override
    public String toString() {
        //TODO ACCOUNT for missing values 
        StringBuilder sb = new StringBuilder();
        
        // First get the maximum length of the column 
        int maxColumnLength = -1;
        int columnPadding = 2;
        int numBars = columns.getValue().size() + 1;
        
        String bar = "|";
        String columnSpace = " ".repeat(columnPadding/2);
        for (CellLangType x: columns.getValue())
        {
            if(maxColumnLength < x.toString().length())
            {
                maxColumnLength = x.toString().length();
            }
        }

        String upperBar = "-".repeat(maxColumnLength * 2 + columnPadding*2 + numBars);
        sb.append("\n"+upperBar+"\n");

        // Print column names header    
        sb.append(bar+columnSpace+columns.getValue().get(0));
        for (int i = 1; i < columns.getValue().size(); i++) {
            sb.append(columnSpace+bar+columnSpace+columns.getValue().get(i));
        }
        sb.append(columnSpace+bar+"\n");
        
        String separator = bar + columnSpace + "-".repeat(maxColumnLength);
        sb.append(separator);
        // Print separator
        for (int i = 1; i < columns.getValue().size(); i++) {
            sb.append(columnSpace+bar+columnSpace+"-".repeat(maxColumnLength));
        }
        sb.append(columnSpace+bar+"\n");
        
        // System.out.println("ROWLIST SHOULD BE A SINGLE LIST");
        // System.out.println(rows.getValue());
        // Print row cell values

        int cellPadding =  new String(columnSpace + "-".repeat(maxColumnLength) + columnSpace).length();

        for (CellLangType row : rows.getValue()) {
            CellList rowList = (CellList) row;
            // System.out.println("ROWLIST SHOULD BE A SINGLE LIST");
            // System.out.println(rowList);
            sb.append(bar);
            for (int i = 0; i < rowList.getValue().size(); i++) {
                sb.append(fitStringToLength(rowList.getValue().get(i).toString(), cellPadding+4)+bar);
                
            }
            sb.append("\n");
        }
    
        return sb.toString();
    }


    public static String fitStringToLength(String input, int length) {
        List<String> result = new ArrayList<>();
        if (input.length() <= length) {
            int padding = (length - input.length()) / 2;
            int extraPadding = (length - input.length()) % 2; // In case of odd number difference
            result.add(String.format("%" + (padding + input.length()) + "s", input));
            result.set(0, String.format("%-" + (padding + input.length() + extraPadding) + "s", result.get(0)));
        } else {
            int startIndex = 0;
            while (startIndex < input.length()) {
                int endIndex = Math.min(startIndex + length, input.length());
                result.add(input.substring(startIndex, endIndex));
                startIndex = endIndex;
            }
        }

        if (result.size() == 1) {
            return result.get(0);
        }

        return String.join("\n", result);
    }

    public CellTable filterTable(CellTable subTable) throws TypeException {
        // System.out.println("ORIGINAL TABLE \n" +this);
        // System.out.println("\n----IN FILTER TABLE \n"+subTable);
        // Create a new table with the same columns as the original table
        CellTable resultTable = new CellTable(this.columns);
    
        // Get the indices of the subTable columns in the original table
        List<Integer> subTableColumnIndices = new ArrayList<>();
        for (CellLangType subTableColumn : subTable.columns.getValue()) {
            int columnIndex = this.columns.getValue().indexOf(subTableColumn);
            if (columnIndex == -1) {
                throw new IllegalArgumentException("Sub table column not found in original table: " + ((CellString) subTableColumn).getValue());
            }
            subTableColumnIndices.add(columnIndex);
        }
        // System.out.println("Column indices "+subTableColumnIndices);
        // System.out.println("ROWS IN SUBTABLE "+subTable.getRows().getValue());
        int x =0;
        for (CellLangType row : this.rows.getValue()) {
            CellList rowList = (CellList) row;
            
            // System.out.println("ROW  ORIGINAL"+row);
                CellLangType subTableRow = subTable.getRows().getValue().get(x);
                CellList subTableRowList = (CellList) subTableRow;
                // System.out.println("ROW IN SUBTABLE "+subTableRowList);
                boolean rowMatches = true;
    
                // Check each column in the subTable
                for (int i = 0; i < subTableColumnIndices.size(); i++) {
                    // int originalTableColumnIndex = subTableColumnIndices.get(i);
                    // CellLangType originalTableCell = rowList.getValue().get(originalTableColumnIndex);
                    CellLangType subTableCell = subTableRowList.getValue().get(i);
                    
                    // System.out.println("Boolean "+subTableCell.getValue());
                    // If the cell values don't match, then this row doesn't match
                    if (! (Boolean)subTableCell.getValue()) {
                        rowMatches = false;
                        break;
                    }
                }
    
                // If all cell values matched, add the original row to the result table
                if (rowMatches) {
                    resultTable.rows.getValue().add(row);
                    //break; // proceed to the next row in the original table
                }
            x++;
        }
    
        return resultTable;
    }

    /// Row in the form [ [ 20, ], [ 10, ], [ 10, ], ]
    // What getcolumns look like[[ 1, ], [ 3, ], [ 5, ]]
    public CellNil mutate(String columnName, List<CellLangType> newColVals) throws TypeException {
        // Find the index of the given column in the original table
        int columnIndex = -1;
        for (int i = 0; i < this.columns.getValue().size(); i++) {
            CellString column = (CellString) this.columns.getValue().get(i);
            if (column.getValue().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }
    
        // If the column does not exist, throw an exception
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column " + columnName + " does not exist in the table.");
        }
    
        //Check that list only contains  one row
         /// Row in the form [ [ 20, ], [ 10, ], [ 10, ], ]
          for (CellLangType cellLangType : newColVals) {

            CellList cl = (CellList) cellLangType;
            if (cl.getValue().size() != 1) {
                throw new IllegalArgumentException("This row should only contain a single value to be added to a column.");
            }
          }
            
        
        // Get Column
            CellTable column = this.getColumn(columnName);

            System.out.println("What columns look like"+ column.getRows().getValue());

          // What column.getRows().getValue() look like[[ 1, ], [ 3, ], [ 5, ]]
          
        // Check if the number of rows in the column and the input rows are equal
        if (column.getRows().getValue().size() != newColVals.size()) {
            throw new IllegalArgumentException("The number of rows in the input does not match the number of rows in the column.");
        }
    
        
    
            // For each row of the table try to get the column value at the index 
            // If the column value exists replace the value within the table 
            // If that column value does not exist it means that it is empty and we next to check the 
            // subsequent rows until a value is found 

            //Keeps track of how many swaps were made in the row
            int swapsMade = 0;
            int rowIndex = 0;
            for (CellLangType cellLangType : this.getRows().getValue()) {

                // Current Row of the table 
                CellList row = (CellList) cellLangType;

                //If the column is blank in that row
                if(columnIndex > row.getValue().size()  )
                {
                    //Skip row
                    continue;
                }
                else
                {   
                    CellList colArr = (CellList)  newColVals.get(swapsMade);
                    
                    //Replace the column index within the row of the original table 
                    // With that of the column index of the newColValues 
                    row.getValue().set(columnIndex,colArr.getValue().get(0));
                  
                    swapsMade ++;
                }

                if(swapsMade == column.getRows().getValue().size())
                {   
                    //We have swapped the number of columns so we exist 
                    break;
                }
                rowIndex++;
            }
            // originalRow.getValue().set(columnIndex, newRow.getValue().get(0));
        

        return new CellNil();
    }

    public int getId()
    {
        return uniqueTableId;
    }
    
    public CellTable applySum() {
        // [[],[]]
        CellList rows = this.getRows(); // assuming getRows() returns List of rows
        List<CellLangType> newRows = new ArrayList<>();
        //System.out.println(rows);
        for (CellLangType r: rows.getValue()) {
            CellList row = (CellList) r;
            double sum = 0;
            for (CellLangType cell : row.getValue()) {
                if (cell instanceof CellDouble) {
                    sum += ((CellDouble) cell).getValue(); // assuming getValue() returns the value of the cell
                } else if (cell instanceof CellInteger) {
                    sum += ((CellInteger) cell).getValue(); 
                }
                // System.out.println("SUM "+ sum);
                // handle other cell types as needed
            }
           CellList x = new CellList();
           x.add( new CellDouble(sum)); //[9]

            newRows.add(x);
            //System.out.println(newRows); // creating a new CellDouble for each sum, change as needed
        }
    
        CellTable outputTable = new CellTable(new CellList(Arrays.asList( new CellString("Sum"))), new CellList(newRows)); // creating a new CellTable with the sums
        // System.out.println(outputTable.getRows());
        // System.out.println(outputTable.getColumns());
        return outputTable;
    }

    public CellTable applyMul() {
        // [[],[]]
        CellList rows = this.getRows(); // assuming getRows() returns List of rows
        List<CellLangType> newRows = new ArrayList<>();
    
        for (CellLangType r: rows.getValue()) {
            CellList row = (CellList) r;
            double sum = 0;
            for (CellLangType cell : row.getValue()) {
                if (cell instanceof CellDouble) {
                    sum *= ((CellDouble) cell).getValue(); // assuming getValue() returns the value of the cell
                } else if (cell instanceof CellInteger) {
                    sum *= ((CellInteger) cell).getValue(); 
                }
                // handle other cell types as needed
            }
            newRows.add(new CellList().add( new CellDouble(sum))); // creating a new CellDouble for each sum, change as needed
        }


    
        CellTable outputTable = new CellTable(this.getColumns(), new CellList(newRows)); // creating a new CellTable with the sums
        return outputTable;
    }
    
    
    
    
}