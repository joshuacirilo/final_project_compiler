public class Diagnostic {
    public final String code;
    public final String message;
    public final SourceSpan span;

    public Diagnostic(String code, String message, SourceSpan span) {
        this.code = code;
        this.message = message;
        this.span = span;
    }

    public String toString() {
        return code + "|" + span.format() + "|" + message;
    }
}
