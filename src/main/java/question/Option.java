package question;

public class Option {
    private String value;
    private Integer weight;

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
}
