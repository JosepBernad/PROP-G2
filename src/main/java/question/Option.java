package question;

/**
 * Classe per representar cada una de les opcions d’una pregunta que té diversos valors
 possibles determintas com a resposta.
 Els atributs són el valor de la possible opció en format String i el pes de la opció (només en cas de ser del tipus SortedQualitativeQuestions , altrament és
 Null )
 */
public class Option {
    private String value;
    private Integer weight;

    public Option() {
    }

    public Option(String value, Integer weight) {
        this.value = value;
        this.weight = weight;
    }

    public Option(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (value != null ? !value.equals(option.value) : option.value != null) return false;
        return weight != null ? weight.equals(option.weight) : option.weight == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
