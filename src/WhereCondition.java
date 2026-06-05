public class WhereCondition {
    public final String column;
    public final String operator;
    public final String literal;
    public final LiteralType literalType;
    public final SourceSpan columnSpan;
    public final SourceSpan operatorSpan;
    public final SourceSpan literalSpan;

    public WhereCondition(String column, String operator, String literal, LiteralType literalType,
                          SourceSpan columnSpan, SourceSpan operatorSpan, SourceSpan literalSpan) {
        this.column = column;
        this.operator = operator;
        this.literal = literal;
        this.literalType = literalType;
        this.columnSpan = columnSpan;
        this.operatorSpan = operatorSpan;
        this.literalSpan = literalSpan;
    }
}
