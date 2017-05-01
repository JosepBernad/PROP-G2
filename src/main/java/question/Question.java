package question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @Type(value = FreeQuestion.class),
        @Type(value = MultivaluedUnsortedQualitativeQuestion.class),
        @Type(value = NumericQuestion.class),
        @Type(value = QualitativeQuestion.class),
        @Type(value = SortedQualitativeQuestion.class),
        @Type(value = UnsortedQualitativeQuestion.class)})

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
