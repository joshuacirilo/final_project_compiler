public class Token {
    public final TokenType type;
    public final String lexeme;
    public final SourceSpan span;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.span = new SourceSpan(line, column);
    }
}
