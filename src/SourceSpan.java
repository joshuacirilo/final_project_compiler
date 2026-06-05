public class SourceSpan {
    public final int line;
    public final int column;

    public SourceSpan(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public String format() {
        return line + ":" + column;
    }
}
