package question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Aquesta classe ens permet instanciar cada una de les preguntes, per poder-les gestionar i
 presentar als usuaris
 El primer atribut és un identificador únic per la pregunta dins una enquesta i el segon atribut la pregunta pròpiament dita que l’usuari ha de contestar
 */
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
