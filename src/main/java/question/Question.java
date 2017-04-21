package question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = FreeQuestion.class, name = "FreeQuestion"),
        @JsonSubTypes.Type(value = MultivaluedUnsortedQualitativeQuestion.class, name = "MultivaluedUnsortedQualitativeQuestion"),
        @JsonSubTypes.Type(value = NumericQuestion.class, name = "NumericQuestion"),
        @JsonSubTypes.Type(value = QualitativeQuestion.class, name = "QualitativeQuestion"),
        @JsonSubTypes.Type(value = SortedQualitativeQuestion.class, name = "SortedQualitativeQuestion"),
        @JsonSubTypes.Type(value = UnsortedQualitativeQuestion.class, name = "UnsortedQualitativeQuestion")})

public abstract class Question {

    private Integer id;
    private String statement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

}
