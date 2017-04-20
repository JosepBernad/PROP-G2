package question;

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
