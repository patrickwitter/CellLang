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
    public CellLangType add(CellLangType v) throws TypeException {
        getValue().add(v);
        return new CellNil();
    }

   
    public CellLangType addAll(CellList v){
        getValue().addAll(v.getValue());
        return new CellNil();
    }

    
}
