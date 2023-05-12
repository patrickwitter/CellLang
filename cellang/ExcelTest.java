import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelTest {
    /// Indicates the column we can start at  
    private int startCol = 0;
    private int currentRow = 0;

    private String WorkbookPath;
    private String SheetName;

    private Workbook workbook;

    private Set<Workbook> workbookSet = new HashSet<>();

    /* 
        This holds the cordinates of the tables we have seen so far 
        Coordinates are of the form 

        COLUMNS AND ROWS ARE ZERO-BASED (Start from Zero) 

        [WorkbookPath,SheetName,TableName] : [ [StartTable], [EndTable]]

        StartTable , EndTable are of the form:
                    [[0,1],[2,1]]
        In the form (# = 'number')
        [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
        In this library columns and rows are specified by numbers so  
                01,21 == A2,C2
        
        The rows number actually tells us the CONTENT Rows of the table that is 
        the contents of the table - header area (1).
        

        
        A1,C1 tells us the table has columns A to C with 1 row 
        it also tells the next table can be written at column E () col 4 

    */
    private static Map<ArrayList<String>,ArrayList<ArrayList<Integer>>> tableCord = new HashMap<>();

    public ExcelTest()
    {

    }

    public void createWorkbook(String workbookPath, String sheetName)
    {
        WorkbookPath = workbookPath;
        SheetName = sheetName;
        System.out.println("WORKBOOKPATH" + WorkbookPath);

        System.out.println(SheetName);

        this.workbook = new XSSFWorkbook();
        workbook.createSheet(sheetName);
        workbookSet.add(workbook);
       
    }

    private Workbook getWorkbook()
    {
        
        return this.workbook;

    }

    private Sheet getSheet(Workbook w)
    {
        Sheet sheet = w.getSheet(this.SheetName);
       if (sheet != null) {
            System.out.println("SHEETNAME "+ sheet.getSheetName());
           return sheet;
       } else {
           // Sheet not found
           System.out.println("Sheet not found: " + SheetName);
           //TODO HANDLE ERROR GRACEFULLY
           return null;
       }
       
       
    }

    
    /**
     * 
     * @param table The CellTable  with literal  values to write
     * 
     * @return void
     * 
     * @post Table with literal is written to Excel
     * @post Table coordinates are added to Map
     */
    
    public void CreateStaticTable(CellTable table)
    {
        Integer startingColumn = getNextColumnStart();
        // Create a new workbook and sheet
        Workbook workbook = getWorkbook();
        Sheet sheet = getSheet(workbook);

        System.out.println("SHEEET IS NULL?");
        System.out.println(sheet == null);
        
        // Write columns to the first row of the sheet
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }
        List<CellLangType> columnsValue = table.getColumns().getValue();

        for (int i = startingColumn; i < columnsValue.size()+startingColumn; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(columnsValue.get(i-startingColumn).toString());  // Assuming CellLangType has a toString() method
        }

        // Setting the next column to be written to the second next column next to it
        // If last column was B Next Column is D
        setNextColumnStart(columnsValue.size()+startingColumn+ 1);


        // Write the rest of the rows to the sheet
        List<CellLangType> rowsValue = table.getRows().getValue();
        for (int i = 0; i < rowsValue.size(); i++) {
            Row row = sheet.getRow(i + 1);  // +1 because the first row is the header
            if (row == null) {
                row = sheet.createRow(i + 1);
            }
            List<CellLangType> cellValues = ((CellList)rowsValue.get(i)).getValue();
            for (int j = 0; j < cellValues.size(); j++) {
                Cell cell = row.createCell(j+startingColumn);
                cell.setCellValue(cellValues.get(j).toString()); 
            }
        }  

        addCoord(Integer.toString(table.getId()), new ArrayList<Integer>(List.of(startingColumn,rowsValue.size())), new ArrayList<Integer>(List.of(startingColumn +columnsValue.size()-1,rowsValue.size())));
        flush(workbook);
    }

    /**
     * 
     * @param table The CellTable with formula values  to write
     * 
     * @return void
     * 
     * @post Table with literal is written to Excel
     * @post Table coordinates are added to Map
     */
    
     public void CreateFormulaTable(CellTable table)
     {
         Integer startingColumn = getNextColumnStart();
         // Create a new workbook and sheet
         Workbook workbook = getWorkbook();
         Sheet sheet = getSheet(workbook);
 
         System.out.println("SHEEET IS NULL?");
         System.out.println(sheet == null);
         
         // Write columns to the first row of the sheet
         Row headerRow = sheet.getRow(0);
         if (headerRow == null) {
             headerRow = sheet.createRow(0);
         }
         List<CellLangType> columnsValue = table.getColumns().getValue();
 
         for (int i = startingColumn; i < columnsValue.size()+startingColumn; i++) {
             Cell headerCell = headerRow.createCell(i);
             headerCell.setCellValue(columnsValue.get(i-startingColumn).toString());  // Assuming CellLangType has a toString() method
         }
 
         // Setting the next column to be written to the second next column next to it
         // If last column was B Next Column is D
         setNextColumnStart(columnsValue.size()+startingColumn+ 1);
 
 
         // Write the rest of the rows to the sheet
         List<CellLangType> rowsValue = table.getRows().getValue();
         for (int i = 0; i < rowsValue.size(); i++) {
             Row row = sheet.getRow(i + 1);  // +1 because the first row is the header
             if (row == null) {
                 row = sheet.createRow(i + 1);
             }
             List<CellLangType> cellValues = ((CellList)rowsValue.get(i)).getValue();
             for (int j = 0; j < cellValues.size(); j++) {
                 Cell cell = row.createCell(j+startingColumn);
                 cell.setCellFormula(cellValues.get(j).toString()); 
             }
         }  
 
         addCoord(Integer.toString(table.getId()), new ArrayList<Integer>(List.of(startingColumn,rowsValue.size())), new ArrayList<Integer>(List.of(startingColumn +columnsValue.size()-1,rowsValue.size())));
         flush(workbook);
     }

     
    public void addTables(CellTable table1, CellTable table2) {
        Workbook workbook = getWorkbook();
        Sheet sheet = getSheet(workbook);
        
        // System.out.println(table1);

        // System.out.println(table2);

        // Checking if both tables are present
        String tableId1 = Integer.toString(table1.getId());
        String tableId2 = Integer.toString(table2.getId());
        
        // System.out.println("TABLE 1 ID: "+tableId1 + " TABLE 2 ID: "+tableId2);
        // If any table is not present, write it to Excel 
        // And put it to mapping 
        if (!isTablePresent(tableId1)) {
            CreateStaticTable(table1);
        }
        if (!isTablePresent(tableId2)) {
            CreateStaticTable(table2);
        }
    
        // Getting coordinates of both tables
        ArrayList<ArrayList<Integer>> table1Cord = getTableCordValue(tableId1);
        ArrayList<ArrayList<Integer>> table2Cord = getTableCordValue(tableId2);

        // System.out.println("COORDINATES ADD T1CORD "+ table1Cord + "\n T2CORD " + table2Cord);
        
        // Getting the number of Content rows in both tables 
        int numCRowsTable1 = getNumContentRows(table1Cord);
        int numCRowsTable2 = getNumContentRows(table2Cord);

        // Getting the number of columns in both tables 
        int numColsTable1 = getNumColumns(table1Cord);
        int numColsTable2 = getNumColumns(table2Cord);

        //Getting the upperbound of column and row of each table
        int maxRows = Math.max(numCRowsTable1,numCRowsTable2);
        int maxCols = Math.max(numColsTable1,numColsTable2);
        
        // /// Points to the row of the each table 
        // int table1RowPointer = 0;
        // int table2RowPointer = 0;

        //  /// Points to the column of the each table 
        //  int table1ColPointer = 0;
        //  int table2ColPointer = 0;

         CellTable result =  new CellTable(new CellList());
         CellList colNms =  new CellList();
        /// Iterate through the longest table and create a table with columnsT1 "+" columnsT2 

        // System.out.println("TABLE1 COLUMN"+table1.getColumns().getValue().toString());
        // System.out.println("TABLE1 COLUMN"+table1.getColumns().getValue().toString());
        for (int i = 0; i < maxCols; i++ )
        {
            String ColT1Nm = i < numColsTable1 ?  table1.getColumns().getValue().get(i).toString() : "";
            String ColT2Nm = i < numColsTable2 ?  table2.getColumns().getValue().get(i).toString() : "";

            try {
                colNms.add(new CellString(ColT1Nm+"+"+ColT2Nm));
            } catch (TypeException e) {
                
                e.printStackTrace();
            }
        }
        
        result.addColumn(colNms);

        /// Going through the maximum rows 
        for (int row = 0; row < maxRows; row++) {

            CellList rowEntry = new CellList();
            Row currentRow = sheet.getRow(row + 1); // Column Headers are in row 0

            if (currentRow == null) {
                currentRow = sheet.createRow(row + 1);
            }

            for (int col = 0; col < maxCols; col++) {

                
                //BUG Row assumed to starts at row 1 Doesn't account for movement of rows 

                // If the currentRow is greater than the
                // We cannot get the cell outright because it might reference a cell in another table 
                // We return null instead
                Cell cellT1  = currentRow.getRowNum() <= numCRowsTable1 && col < numColsTable1 ? currentRow.getCell(col + getColumnStart(table1Cord)) : null;
                Cell cellT2 =  currentRow.getRowNum() <= numCRowsTable2 && col < numColsTable2 ? currentRow.getCell(col + getColumnStart(table2Cord)) : null;

               
               

                // If out of bounds Celladdress should be a literal 0 otherwise get the cellAddress 
                String cellT1Address;
                String cellT2Address;
                if(cellT1 == null)
                {   
                    System.out.println("CELL TABLE 1 NULL");
                    cellT1Address = "0";
                }
                else{
                    System.out.println("CELL TABLE 1 | " + cellT1.getRowIndex() + " " + cellT1.getColumnIndex());

                    cellT1Address = new CellReference(cellT1.getRowIndex(), cellT1.getColumnIndex()).formatAsString();
                }

                if(cellT2 == null)
                {
                    System.out.println("CELL TABLE 2 NULL");
                    cellT2Address = "0";
                }
                else{
                    System.out.println("CELL TABLE 2 | " + cellT2.getRowIndex() + " " + cellT2.getColumnIndex());
                    cellT2Address = new CellReference(cellT2.getRowIndex(), cellT2.getColumnIndex()).formatAsString();
                }


                // String cell1Address = new CellReference(row, col).formatAsString();
                // String cell2Address = new CellReference(row + 1, table2Cord.get(0).get(0) + col).formatAsString();

                // String formula = "IF(ISBLANK(" + cell1Address + "),0," + cell1Address + ") + IF(ISBLANK(" + cell2Address + "),0," + cell2Address + ")";
                String formula = "SUM("+cellT1Address+","+cellT2Address+")";
                // cell.setCellFormula(formula);
                try {
                    rowEntry.add(new CellString(formula));
                } catch (TypeException e) {
                  
                    e.printStackTrace();
                }
            }

            result.addRow(rowEntry);
        }
    
       CreateFormulaTable(result);
    }


    /**
     * This method writes all changes to the workbook.
     * @pre Workbook has been created
     * @param workbook The workbook to be written to 
     * @return void
     */
    private void flush(Workbook workbook)
    {
        try (FileOutputStream file = new FileOutputStream(WorkbookPath)) {
            workbook.write(file);
            file.close();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        System.out.println("Excel file generated/modified successfully.");
    }

    public void closeAll()
    {
        for (Workbook w : workbookSet) {
            try {
                w.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for ( Map.Entry<ArrayList<String>,ArrayList<ArrayList<Integer>>> entry : tableCord.entrySet()) {
            System.out.println("Key = " + entry.getKey() + "\n, Value = \n" + entry.getValue());
        }
    }
    /**
     * @pre TableId must be unique 
     * @param tableId The Id of the table to be added 
     * @param startofTable Describes where the column starts and how many rows - 1 are in the table (excluding header row) 
     * @param endofTable  Describes where the column end ands how many rows - 1 are in the table (excluding header row) 
     * @post Table Coordinate is added to the map 
     */
    private void addCoord (String tableId,ArrayList<Integer> startofTable,ArrayList<Integer> endofTable)
    {
        tableCord.put(new ArrayList<String>(List.of(WorkbookPath,SheetName,tableId)), new ArrayList<ArrayList<Integer>>(List.of(startofTable,endofTable)));
    }


    /**
     * This method checks if a table with the given ID is present.
     *
     * @param tableId The ID of the table to check.
     * @return true if the table is not present, false otherwise.
     */
    private Boolean isTablePresent(String tableId)
    {
        return getTableCordValue(tableId) != null;
    }

    /**
     * @pre The Table is present within the map 
     * @param tableID The Id of the table
     * @return The coordinate of the table in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     */
    private ArrayList<ArrayList<Integer>> getTableCordValue(String tableID)
    {
        return tableCord.get(getTableCordKey(tableID));
    }

    private ArrayList<String> getTableCordKey(String tableId)
    {
        return new ArrayList<String>(List.of(this.WorkbookPath,this.SheetName,tableId));
    }

    /**
     * @pre Coordinate are stored in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     * @param tableCord The coordinates of the table within the sheet
     * @return The numerical index of where the table starts (The first column)
     */
    private int getColumnStart(ArrayList<ArrayList<Integer>> tableCord)
    {
        return tableCord.get(0).get(0);
    }

    /**
     * @pre Coordinate are stored in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     * @param tableCord The coordinates of the table within the sheet
     * @return The numerical index of where the table ends (The last column)
     */
    private int getColumnEnd(ArrayList<ArrayList<Integer>> tableCord)
    {
        return tableCord.get(1).get(0);
    }

    /**
     * @pre Coordinate are stored in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     * @param tableCord The coordinates of the table within the sheet
     * @return The number of columns in the table
     */
    private int getNumColumns(ArrayList<ArrayList<Integer>> tableCord)
    {
        return (getColumnEnd(tableCord) - getColumnStart(tableCord))+1;
    }

    /**
     * @pre Coordinate are stored in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     * @param tableCord The coordinates of the table within the sheet
     * @return The number of rows in the table that have values (excluding header row)
     */
    private int getNumContentRows (ArrayList<ArrayList<Integer>> tableCord)
    {
        return tableCord.get(0).get(1);
    }

    /**
     * @pre Coordinate are stored in form [ColumnStart,# of Rows -1],[ColumnEnd, # Row of Rows -1]
     * @param tableCord The coordinates of the table within the sheet
     * @return The number of rows in the table (including header row)
     */
    private int getNumRows (ArrayList<ArrayList<Integer>> tableCord)
    {
        return getNumContentRows(tableCord) + 1;
    }


    


    //BUG Potential Error
    private int getNextColumnStart()
    {
        return startCol;
    }

   

    private void setNextColumnStart( int col)
    {
         startCol = col;
    }
}
