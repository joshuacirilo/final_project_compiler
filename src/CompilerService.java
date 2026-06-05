import java.util.List;

public class CompilerService {
    public ValidationResult validate(String sql) {
        ValidationResult result = new ValidationResult();
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(sql);
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.type == TokenType.INVALID) {
                result.diagnostics.add(new Diagnostic("LEXICAL_INVALID_TOKEN", "Token inválido: " + t.lexeme, t.span));
            }
        }
        if (result.diagnostics.size() == 0) {
            Parser parser = new Parser();
            result.ast = parser.parse(tokens, result);
        }
        if (result.diagnostics.size() == 0) {
            new SemanticValidator().validate(result.ast, result);
        }
        result.valid = result.diagnostics.size() == 0;
        return result;
    }
}
