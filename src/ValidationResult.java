import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    public boolean valid;
    public SelectStatement ast;
    public final List<Diagnostic> diagnostics = new ArrayList<Diagnostic>();
    public final List<String> traces = new ArrayList<String>();
}
