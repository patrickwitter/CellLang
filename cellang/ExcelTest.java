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

        [WorkbookPath,SheetName,TableName] : [ [StartTable], [EndTable]]

        StartTable , EndTable are of the form:
                    [[0,1],[2,1]]
        In the form (# = 'number')
        Column#Row#,Column#Row#
        In this library columns and rows are specified by numbers so  
                01,21 == A1,C1 
        
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

    
    
    /// Creates a Table with literal values 
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

    public void addTables(CellTable table1, CellTable table2) {
        Workbook workbook = getWorkbook();
        Sheet sheet = getSheet(workbook);
    
        // Checking if both tables are present
       String tableId1 = Integer.toString(table1.getId());
        String tableId2 = Integer.toString(table2.getId());
    
        // If any table is not present, write it
        if (!isTablePresent(tableId1)) {
            CreateStaticTable(table1);
        }
        if (!isTablePresent(tableId2)) {
            CreateStaticTable(table2);
        }
    
        // Getting coordinates of both tables
        ArrayList<ArrayList<Integer>> table1Cord = getTableCordValue(tableId1);
        ArrayList<ArrayList<Integer>> table2Cord = getTableCordValue(tableId2);
    
        int maxRows = Math.max(table1Cord.get(1).get(1), table2Cord.get(1).get(1));
        int maxCols = Math.max(table1Cord.get(1).get(0), table2Cord.get(1).get(0));
    
        for (int row = 0; row < maxRows; row++) {
            Row currentRow = sheet.getRow(row + 1); // Column Headers are in row 0
            if (currentRow == null) {
                currentRow = sheet.createRow(row + 1);
            }
            for (int col = 0; col < maxCols; col++) {
                Cell cell = currentRow.getCell(col + startCol);
                if (cell == null) {
                    cell = currentRow.createCell(col + startCol);
                }
    
                // Creating formula string
                String cell1Address = new CellReference(row + 1, table1Cord.get(0).get(0) + col).formatAsString();
                String cell2Address = new CellReference(row + 1, table2Cord.get(0).get(0) + col).formatAsString();
                String formula = "IF(ISBLANK(" + cell1Address + "),0," + cell1Address + ") + IF(ISBLANK(" + cell2Address + "),0," + cell2Address + ")";
                cell.setCellFormula(formula);
            }
        }
    
        flush(workbook);
    }


    /**
     * This method writes all changes to the workbook.
     *
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
        return getTableCordValue(tableId) == null;
    }

    private ArrayList<ArrayList<Integer>> getTableCordValue(String tableID)
    {
        return tableCord.get(getTableCordKey(tableID));
    }

    private ArrayList<String> getTableCordKey(String tableId)
    {
        return new ArrayList<String>(List.of(this.WorkbookPath,this.SheetName,tableId));
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
