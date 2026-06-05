import java.util.ArrayList;
import java.util.List;

public class ConditionChain {
    public final List<WhereCondition> conditions = new ArrayList<WhereCondition>();
    public final List<String> connectors = new ArrayList<String>();
}
