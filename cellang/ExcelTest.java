import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ExcelTest {
    /// Indicates the column we can start at  
    private int startCol = 0;
    private int currentRow = 0;

    private String WorkbookPath;
    private String SheetName;

    private Workbook workbook;

    private Set<Workbook> workbookSet = new HashSet<>();

    /**
     * Names of variables seen within a mappings file
     */
    private Set<String> tableVariablesSeen = new HashSet<>();

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

    private Workbook getWorkbook(String workbookPath)
    {
    
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(workbookPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;

    }



    private Sheet getSheet(Workbook w)
    {
        Sheet sheet = w.getSheet(this.SheetName);
       if (sheet != null) {
            // System.out.println("SHEETNAME "+ sheet.getSheetName());
           return sheet;
       } else {
           // Sheet not found
        //    System.out.println("Sheet not found: " + SheetName);
           //TODO HANDLE ERROR GRACEFULLY
           return null;
       }

    }
       private Sheet getSheet(Workbook w, String sheetName)
       {
           Sheet sheet = w.getSheet(sheetName);
          if (sheet != null) {
               // System.out.println("SHEETNAME "+ sheet.getSheetName());
              return sheet;
          } else {
              // Sheet not found
           //    System.out.println("Sheet not found: " + SheetName);
              //TODO HANDLE ERROR GRACEFULLY
              return null;
          }
       
       
    }

    
    /**
     * 
     * @param table The CellTable  with literal  values to write
     * @param paths Optional array containing the path of the workbook and name of the sheet respectively
     * @return void
     * 
     * @post Table with literal is written to Excel
     * @post Table coordinates are added to Map
     * 
     * 
     */
    
    public void CreateStaticTable(CellTable table,String ... paths)
    {
        String customWorkbookPath = null;
        String customSheetName = null ;
        
        if(paths != null ){
            if(paths.length == 2)
            {
                customWorkbookPath  = paths[0];
            customSheetName= paths[1];
            }
           
            
        }
         

        Integer startingColumn = getNextColumnStart();
        Workbook workbook ;
        Sheet sheet ;
        if(customSheetName != null && customWorkbookPath != null){
             // Create a new workbook and sheet
            workbook = getWorkbook(customWorkbookPath);
            sheet = getSheet(workbook,customSheetName);
        }

        else
        {
             workbook = getWorkbook();
             sheet = getSheet(workbook);
        }
       

        // System.out.println("SHEEET IS NULL?");
        // System.out.println(sheet == null);
        
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
                //TODO Change to literal value
               
                setCellValue(cell,cellValues.get(j).getValue()); 
            }
        }  

        addCoord(Integer.toString(table.getId()), new ArrayList<Integer>(List.of(startingColumn,rowsValue.size())), new ArrayList<Integer>(List.of(startingColumn +columnsValue.size()-1,rowsValue.size())));
        flush(workbook);
    }

    /**
     * @pre cell is not null
     * @param cell The cell to be written to
     * @param value The value to be written to the cell
     * 
     * 
     */
    private void setCellValue(Cell cell , Object value)
    {

     
        if (value instanceof Integer){
            cell.setCellValue((Integer)value);
        }
        else if (value instanceof Double)
        {
            cell.setCellValue((Double)value);
        }
        else if (value instanceof Boolean)
        {
            cell.setCellValue((Boolean)value);
        }
        else
        {
            cell.setCellValue(value.toString());
        }
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

    
    public void addTables(CellTable table1, CellLangType t) {
        Workbook workbook = getWorkbook();
        Sheet sheet = getSheet(workbook);
        
        if(t instanceof CellTable)
        {
            CellTable table2 = (CellTable) t;
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

                colNms.add(new CellString(ColT1Nm+"+"+ColT2Nm));
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

                    String formula = "SUM("+cellT1Address+","+cellT2Address+")";
                    rowEntry.add(new CellString(formula));
                }

                result.addRow(rowEntry);
            }
        
            CreateFormulaTable(result);
        }
//---------------------- TODO ADDING SCALARS
        else 
        {
            System.out.println("---------ADDING SCALAR TO TABLE SCLALR "+t.toString() );
            //Create A table for the scalar called Contstant 
            CellList colConst = new CellList(new ArrayList<>(List.of(new CellString("Constant"))));
            
            CellList rowConst = new CellList();
            CellList rowE = new CellList();
            rowE.add(t);

            rowConst.add(rowE);

            System.out.println("---CONSTANNT ROW "+rowConst);

            CellTable constant = new CellTable(colConst,new CellList(rowConst.getValue()));
            CreateStaticTable(constant);

            String tableId1 = Integer.toString(table1.getId());
            String constantId = Integer.toString(constant.getId());

            //IF Table not present create it 
            if (!isTablePresent(tableId1)) {
                CreateStaticTable(table1);
            }
        
            // Getting coordinates of both tables
            ArrayList<ArrayList<Integer>> table1Cord = getTableCordValue(tableId1);
            ArrayList<ArrayList<Integer>> constantCord = getTableCordValue(constantId);
            // Iterate over the row ,column of table and add the constant 

            int numCRowsTable1 = getNumContentRows(table1Cord);

            int numColsTable1 = getNumColumns(table1Cord);

            CellTable result =  new CellTable(new CellList());

            // Let the columns remain the same 
            result.addColumn(table1.getColumns());
            
            Row constantRow = sheet.getRow(1);
            /// Going through the rows of table1 since the constant will be absolute
            for (int row = 0; row < numCRowsTable1; row++) {

                CellList rowEntry = new CellList();
                Row currentRow = sheet.getRow(row + 1); // Column Headers are in row 0

                if (currentRow == null) {
                    currentRow = sheet.createRow(row + 1);
                }
                /// Going through the rows of table1 since the constant will be absolute
                for (int col = 0; col < numColsTable1; col++) {

                    
                    Cell cellT1  =  currentRow.getCell(col + getColumnStart(table1Cord));

                    //COnstant is 
                    Cell cellConstant =  constantRow.getCell(getColumnStart(constantCord));

                
                

                    // If out of bounds Celladdress should be a literal 0 otherwise get the cellAddress 
                    String cellT1Address;
                    String constantAddress;
                    if(cellT1 == null)
                    {   
                        System.out.println("CELL TABLE 1 NULL");
                        cellT1Address = "0";
                    }
                    else{
                        System.out.println("CELL TABLE 1 | " + cellT1.getRowIndex() + " " + cellT1.getColumnIndex());

                        cellT1Address = new CellReference(cellT1.getRowIndex(), cellT1.getColumnIndex()).formatAsString();
                    }

                    if(cellConstant == null)
                    {
                        System.out.println("CELL TABLE 2 NULL");
                        constantAddress = "0";
                    }
                    else{
                        System.out.println("CELL TABLE 2 | " + cellConstant.getRowIndex() + " " + cellConstant.getColumnIndex());
                        constantAddress = new CellReference(cellConstant.getRowIndex(), cellConstant.getColumnIndex()).formatAsString();
                    }

                    System.out.println("CONSTANT ADDRESS "+constantAddress);
                    String formula = "SUM("+cellT1Address+","+"$"+constantAddress.charAt(0)+"$"+constantAddress.charAt(1)+")";
                    rowEntry.add(new CellString(formula));
                }

                result.addRow(rowEntry);
            }
        
            CreateFormulaTable(result);
        }
        
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
               
                e.printStackTrace();
            }
        }

        for ( Map.Entry<ArrayList<String>,ArrayList<ArrayList<Integer>>> entry : tableCord.entrySet()) {
            System.out.println("Key = " + entry.getKey() + "\n, Value = \n" + entry.getValue());
        }

        if(!tableCord.isEmpty())
        {
            toMappingsFile();
            System.out.println("Mappings file ran");
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

//-------------------------- WRITE TO MAPPINGS FILE 

    /**
     * @pre The mappings datastructure is not null
     * 
     * 
     */
    private void toMappingsFile()
    {
        Map<String, ArrayList<String>> groupedTables = new HashMap<>();
        for (Map.Entry<ArrayList<String>, ArrayList<ArrayList<Integer>>> entry : tableCord.entrySet()) {
            ArrayList<String> key = entry.getKey();
            ArrayList<ArrayList<Integer>> value = entry.getValue();
            String workbookPath = key.get(0);
            String sheetName = key.get(1);
            String tableId = key.get(2);
            String mapKey = workbookPath + "," + sheetName;
    
            String coordinateRepresentation = String.format("@%s = [%s:%s];", tableId, convertToExcelNotation(value.get(0)), convertToExcelNotation(value.get(1)));
    
            if (!groupedTables.containsKey(mapKey)) {
                groupedTables.put(mapKey, new ArrayList<>());
            }
    
            groupedTables.get(mapKey).add(coordinateRepresentation);
        }
    
        for (Map.Entry<String, ArrayList<String>> entry : groupedTables.entrySet()) {
            String mapKey = entry.getKey();
            String[] splitKey = mapKey.split(",");
            String workbookPath = splitKey[0];
            String sheetName = splitKey[1];
            String outputFilePath = Paths.get(workbookPath).getParent().toString() + "/" + Paths.get(workbookPath).getFileName().toString().replace(".xlsx", "Map.txt");
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
                writer.println("from (" + workbookPath + "," + sheetName + ");");
                for (String tableCoordinate : entry.getValue()) {
                    writer.println(tableCoordinate);
                }
                writer.println("end;");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String convertToExcelNotation(ArrayList<Integer> coordinate) {
        int colIndex = coordinate.get(0);
        int rowIndex = coordinate.get(1) + 1; // adding 1 because Excel is 1-indexed, not 0-indexed
    
        StringBuilder columnName = new StringBuilder();
        while (colIndex >= 0) {
            int charIndex = (colIndex % 26);
            columnName.append((char) ('A' + charIndex));
            colIndex /= 26;
            colIndex--;
        }
    
        return columnName.reverse().toString() + rowIndex;
    }
    //------------------END 


//---------------------- READ FROM MAPPINGS FILE  PLACE COORDINATES INTO TABLECORDFILE
    public void readTableCoordinatesFromMappingFile(String mappingFilePath) {
        try (Scanner scanner = new Scanner(new File(mappingFilePath))) {
            ArrayList<String> key = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("from")) {
                    String[] split = line.substring(6, line.length() - 2).split(",");
                    key = new ArrayList<>(Arrays.asList(split[0], split[1], ""));
                } else if (line.startsWith("@")) {
                    String[] split = line.split(" ");
                    key.set(2, split[0].substring(1));
                    String[] coordinates = split[2].substring(1, split[2].length() - 3).split(":");
                    ArrayList<ArrayList<Integer>> value = new ArrayList<>();
                    value.add(convertFromExcelNotation(coordinates[0]));
                    value.add(convertFromExcelNotation(coordinates[1]));
                    tableCord.put(new ArrayList<>(key), value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private ArrayList<Integer> convertFromExcelNotation(String cellReference) {
        int colIndex = 0;
        int i = 0;
        while (i < cellReference.length() && Character.isLetter(cellReference.charAt(i))) {
            colIndex = colIndex * 26 + (cellReference.charAt(i) - 'A' + 1);
            i++;
        }
        colIndex--; // subtracting 1 because Excel is 1-indexed, not 0-indexed
    
        int rowIndex = Integer.parseInt(cellReference.substring(i)) - 1; // subtracting 1 because Excel is 1-indexed, not 0-indexed
        return new ArrayList<>(Arrays.asList(colIndex, rowIndex));
    }

    //------------ END 

//------------------ READ FROM MAPPINGS FILE  GET CELLTABLES 

public List<Pair<String, CellTable>> readTablesFromMappingsFile(String mappingFilePath) {
    List<Pair<String, CellTable>> tables = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(mappingFilePath))) {
        String line;
        String workbookPath = "";
        String sheetName = "";

        Workbook workbookToRead = null;
        Sheet sheet = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("from")) {
                // Close previous workbook if it was opened
                if (workbookToRead != null) {
                    workbookToRead.close();
                }

                // Parse workbook path and sheet name
                int start = line.indexOf('(') + 1;
                int end = line.indexOf(')');
                String[] parts = line.substring(start, end).split(",");
                workbookPath = parts[0].trim();
                sheetName = parts[1].trim();

                // Open workbook and get sheet
                workbookToRead = WorkbookFactory.create(new File(workbookPath));
                sheet = workbookToRead.getSheet(sheetName);
            } else if (line.startsWith("@")) {
                // Parse table ID and cell range
                int equalsIndex = line.indexOf('=');
                String tableId = line.substring(0, equalsIndex).trim();

                System.out.println("---TABLE ID READ FROM MAPPINGS FILE "+tableId);

                // Check for uniqueness
                if (tableVariablesSeen.contains(tableId)) {
                    throw new IllegalArgumentException("All variable names should be unique, duplicate found: " + tableId);
                } else {
                    tableVariablesSeen.add(tableId);
                }

                String range = line.substring(equalsIndex + 1, line.length() - 1).trim();
                
                // Parse start and end cell references
                String[] cellReferences = range.substring(1, range.length() - 1).split(":");
                System.out.println("CELLRANGE FOR ID "+ tableId +" "+ Arrays.toString(cellReferences));
                ArrayList<Integer> startCoordinate = convertFromExcelNotation(cellReferences[0]);
                ArrayList<Integer> endCoordinate = convertFromExcelNotation(cellReferences[1]);
                System.out.println("CELL COORD FOR ID "+ tableId +" "+startCoordinate.toString() + " " + endCoordinate.toString());
                // Read table data from sheet
                CellTable table = readTableFromSheet(sheet, startCoordinate, endCoordinate,workbookToRead);
                try {
                    // The tag should be equal the table id 
                    table.setTableTag(tableId);
                } catch (RuntimeException e) {
                   
                    e.printStackTrace();
                } // Adding the tag to the table

                tables.add(new Pair<>(tableId, table));
            }
        }

        // Close last workbook
        if (workbookToRead != null) {
            workbookToRead.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return tables;
}

private CellTable readTableFromSheet(Sheet sheet, ArrayList<Integer> startCoordinate, ArrayList<Integer> endCoordinate, Workbook workbookToRead) {
    CellList columns = new CellList();
    CellList rows = new CellList();

    int startRow = endCoordinate.get(1) - startCoordinate.get(1);

    for (int rowIndex = startRow; rowIndex <= endCoordinate.get(1); rowIndex++) {
        Row row = sheet.getRow(rowIndex);
        CellList tableRow = new CellList();

     
        for (int colIndex = startCoordinate.get(0); colIndex <= endCoordinate.get(0); colIndex++) {
            Cell cell = row.getCell(colIndex);
            CellLangType tableCell;

            if (rowIndex == startRow) {
                tableCell = new CellString(cell.getStringCellValue());
                columns.add(tableCell);
            } else {
                //TODO 
                switch (cell.getCellType()) {
                    case BOOLEAN:
                        tableCell = new CellBoolean(cell.getBooleanCellValue());
                        break;
                    case STRING:
                        tableCell = new CellString(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        double numericValue = cell.getNumericCellValue();
                        tableCell = numericValue == (int) numericValue ? new CellInteger((int) numericValue) : new CellDouble(numericValue);
                        break;
                    case FORMULA:
                        FormulaEvaluator evaluator = workbookToRead.getCreationHelper().createFormulaEvaluator();
                        CellValue cellValue = evaluator.evaluate(cell);
                        switch (cellValue.getCellType()) {
                            case BOOLEAN:
                                tableCell = new CellBoolean(cellValue.getBooleanValue());
                                break;
                            case STRING:
                                tableCell = new CellString(cellValue.getStringValue());
                                break;
                            case NUMERIC:
                                double formulaNumericValue = cellValue.getNumberValue();
                                tableCell = formulaNumericValue == (int) formulaNumericValue ? new CellInteger((int) formulaNumericValue) : new CellDouble(formulaNumericValue);
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported cell value type: " + cellValue.getCellType());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported cell type: " + cell.getCellType());
                }

                tableRow.add(tableCell);
            }
        }
        if (rowIndex != 0) {
            rows.add(tableRow);
        }
    }
    System.out.println("Contents of Table " + rows + " "+ columns);
    return new CellTable(columns, rows);
}

//------------------------------- END 

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
