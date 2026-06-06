import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos;
    private ValidationResult result;

    public SelectStatement parse(List<Token> tokens, ValidationResult result) {
        this.tokens = tokens;
        this.pos = 0;
        this.result = result;
        SelectStatement statement = new SelectStatement();
        expect(TokenType.SELECT, "SYNTACTIC_EXPECTED_SELECT");
        parseColumns(statement);
        expect(TokenType.FROM, "SYNTACTIC_EXPECTED_FROM");
        Token table = expect(TokenType.IDENTIFIER, "SYNTACTIC_EXPECTED_TABLE");
        if (table != null) statement.table = table.lexeme;

        // TODO SERIE 2:
        // Implementar parseo de WHERE opcional:
        // WHERE <columna> <operador> <literal> (AND|OR <columna> <operador> <literal>)*
        // Debe llenar statement.where con SourceSpan exactos.
        if (match(TokenType.WHERE)) {
            statement.where = new ConditionChain();
            parseWhereClause(statement.where);
        }

        if (check(TokenType.SEMICOLON)) advance();
        if (!check(TokenType.EOF)) {
            result.diagnostics.add(new Diagnostic("SYNTACTIC_UNEXPECTED_TOKEN", "Token inesperado: " + current().lexeme, current().span));
        }
        return statement;
    }

    private void parseColumns(SelectStatement statement) {
        if (match(TokenType.STAR)) {
            statement.columns.add("*");
            return;
        }
        Token first = expect(TokenType.IDENTIFIER, "SYNTACTIC_EXPECTED_COLUMN");
        if (first != null) statement.columns.add(first.lexeme);
        while (match(TokenType.COMMA)) {
            Token next = expect(TokenType.IDENTIFIER, "SYNTACTIC_EXPECTED_COLUMN");
            if (next != null) statement.columns.add(next.lexeme);
        }
    }

    private void parseWhereClause(ConditionChain where) {
        WhereCondition condition = parseWhereCondition();
        if (condition == null) return;
        where.conditions.add(condition);

        while (match(TokenType.AND) || match(TokenType.OR)) {
            Token connectorToken = tokens.get(pos - 1);
            where.connectors.add(connectorToken.lexeme.toUpperCase());
            WhereCondition nextCondition = parseWhereCondition();
            if (nextCondition == null) return;
            where.conditions.add(nextCondition);
        }
    }

    private WhereCondition parseWhereCondition() {
        Token column = expect(TokenType.IDENTIFIER, "SYNTACTIC_EXPECTED_WHERE_OPERAND");
        if (column == null) return null;

        Token operator = parseWhereOperator();
        if (operator == null) return null;

        Token literal = parseWhereLiteral();
        if (literal == null) return null;

        LiteralType literalType = inferLiteralType(literal.type);
        return new WhereCondition(column.lexeme, operator.lexeme, literal.lexeme, literalType,
                column.span, operator.span, literal.span);
    }

    private Token parseWhereOperator() {
        if (check(TokenType.EQUAL) || check(TokenType.GREATER) || check(TokenType.LESS)
                || check(TokenType.GREATER_EQUAL) || check(TokenType.LESS_EQUAL) || check(TokenType.NOT_EQUAL)) {
            return advance();
        }
        result.diagnostics.add(new Diagnostic("SYNTACTIC_EXPECTED_WHERE_OPERAND",
                "Se esperaba operador de comparación y se encontró " + current().type,
                current().span));
        return null;
    }

    private Token parseWhereLiteral() {
        if (match(TokenType.NUMBER) || match(TokenType.STRING) || match(TokenType.TRUE) || match(TokenType.FALSE)) {
            return tokens.get(pos - 1);
        }
        result.diagnostics.add(new Diagnostic("SYNTACTIC_EXPECTED_WHERE_OPERAND",
                "Se esperaba literal y se encontró " + current().type,
                current().span));
        return null;
    }

    private LiteralType inferLiteralType(TokenType type) {
        if (type == TokenType.NUMBER) return LiteralType.NUMBER;
        if (type == TokenType.STRING) return LiteralType.STRING;
        if (type == TokenType.TRUE || type == TokenType.FALSE) return LiteralType.BOOLEAN;
        return LiteralType.UNKNOWN;
    }

    private Token expect(TokenType type, String code) {
        if (check(type)) return advance();
        result.diagnostics.add(new Diagnostic(code, "Se esperaba " + type + " y se encontró " + current().type, current().span));
        return null;
    }

    private boolean match(TokenType type) { if (check(type)) { advance(); return true; } return false; }
    private boolean check(TokenType type) { return current().type == type; }
    private Token current() { return tokens.get(pos); }
    private Token advance() { return tokens.get(pos++); }
}
