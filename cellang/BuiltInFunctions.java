import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BuiltInFunctions {
    static String apply = "apply";
    static Set builtInFunctions =  new HashSet<>(Arrays.asList(apply));

    static String mutate = "mut";
    static String getRow = "getRow";
    static String getCol = "getCol";
    static Set builtInTableFunctions =  new HashSet<>(Arrays.asList(mutate,getRow,getCol));
}
