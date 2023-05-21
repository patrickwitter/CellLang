import java.util.ArrayList;
import java.util.List;

public class CellList extends CellLangType<List<CellLangType>>{

    public boolean isabs = false;
    private static int globalTableId = 0;
    
    private int uniqueTableId;

    private String tableTag;

        /**
     * @pre Table X has been created 
     * 
     * @pre Table Y has been created 
     * 
     * @post Two tables will share the same id 
     * 
     * Bounds table with some relationship to the same ID
     * The tag is a string representaion of a Table ID 
     *   
     * @param tag The table id to be referenced 
     */
    public void setTableTag(String tag) 
    {
  
            tableTag = tag;
            //tableTagsSeen.add(tag);    
    }

    public String getTag()
    {
        if(tableTag == null)
        {
            return Integer.toString(uniqueTableId);
        }

        return tableTag;
    }

    public boolean hasTag()
    {
        return tableTag != null;
    }

    public int getId()
    {
        return uniqueTableId;
    }
    
    public CellList(){
        
        super(new ArrayList<>());
        globalTableId++;
    }

    public CellList(List<CellLangType> l){
        
        super(l);
        globalTableId++;
    }
    
    @Override
    public String toString() {
        String result = "[";

        List<CellLangType> list = getValue();

        for(CellLangType x: list)
        {
            result += " "+x.toString()+",";;
        }

        result += " ]";

        return result;
    }

    @Override
    public CellLangType add(CellLangType v)  {
        getValue().add(v);
        return new CellNil();
    }

   
    public CellLangType addAll(CellList v){
        getValue().addAll(v.getValue());
        return new CellNil();
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * @param start The starting index (inclusive)
     * @param end The ending index (inclusive)
     * @return The sublist from index start to index end
     */
    public CellLangType slice(int start, int end)
    {
        return new CellList(this.getValue().subList(start, end+1));
    }

    
}
