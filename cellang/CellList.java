import java.util.List;

public class CellList extends CellLangType<List<CellLangType>>{
    public CellList(List<CellLangType> l ){
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
}
