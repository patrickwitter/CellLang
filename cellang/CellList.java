import java.util.ArrayList;
import java.util.List;

public class CellList extends CellLangType<List<CellLangType>>{

    public CellList(){
        
        super(new ArrayList<>());
    }

    public CellList(List<CellLangType> l){
        
        super(l);
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
