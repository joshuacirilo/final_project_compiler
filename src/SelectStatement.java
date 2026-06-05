import java.util.ArrayList;
import java.util.List;

public class SelectStatement {
    public final List<String> columns = new ArrayList<String>();
    public String table;
    public ConditionChain where;
}
