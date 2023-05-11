import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcelTest {
    /// Indicates the column we can start at  
    private int startCol = 0;
   
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
    private static HashMap<ArrayList<String>,ArrayList<ArrayList<Integer>>> tableCord = new HashMap<>();

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
        // try (FileOutputStream file = new FileOutputStream(workbookPath)) {
        //     workbook.write(file);
        //     // file.close();
        // } catch (IOException e) {
            
        //     e.printStackTrace();
        // }

        // try {
        //     workbook.close();
        // } catch (IOException e) {
           
        //     e.printStackTrace();
        // }
    }

    private Workbook getWorkbook()
    {
        // try (FileInputStream fileInputStream = new FileInputStream(WorkbookPath);
        // Workbook workbook = new XSSFWorkbook(fileInputStream)) {
        // return workbook;
       

        // } catch (IOException e) {
        //     System.out.println("ERROR IN GETWORKBOOK");
        //     e.printStackTrace();
        // }

        // //TODO HANDLE ERROR GRACEFULLY
        // return null;

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
           return null;
       }
       //TODO HANDLE ERROR GRACEFULLY
       
    }

    
    
    /// Creates a Table with literal values 
    public void CreateStaticTable(CellTable table)
    {
        // Create a new workbook and sheet
        Workbook workbook = getWorkbook();
        Sheet sheet = getSheet(workbook);

        System.out.println("SHEEET IS NULL?");
        System.out.println(sheet == null);
        
    // Write columns to the first row of the sheet
    Row headerRow = sheet.createRow(0);
    List<CellLangType> columnsValue = table.getColumns().getValue();
    for (int i = getNextColumnStart(); i < columnsValue.size(); i++) {
        Cell headerCell = headerRow.createCell(i);
        headerCell.setCellValue(columnsValue.get(i).toString());  // Assuming CellLangType has a toString() method
    }

    // Setting the next column to be written to the column next to it
    setNextColumnStart(columnsValue.size() + 1);
    // Write the rest of the rows to the sheet
    List<CellLangType> rowsValue = table.getRows().getValue();
    for (int i = 0; i < rowsValue.size(); i++) {
        Row row = sheet.createRow(i + 1);  // +1 because the first row is the header
        List<CellLangType> cellValues = ((CellList)rowsValue.get(i)).getValue();
        for (int j = 0; j < cellValues.size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(cellValues.get(j).toString()); 
        }
    }  
    
    flush(workbook);
    }

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
    }

    private void addCoord (String tableName,ArrayList<Integer> startofTable,ArrayList<Integer> endofTable)
    {
        tableCord.put(new ArrayList<String>(List.of(WorkbookPath,SheetName,tableName)), new ArrayList<ArrayList<Integer>>(List.of(startofTable,endofTable)));
    }



    private int getNextColumnStart()
    {
        return startCol++;
    }

    private void setNextColumnStart( int col)
    {
         startCol = col;
    }
}
