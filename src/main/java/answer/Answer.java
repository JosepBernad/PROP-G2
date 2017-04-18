package answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = FreeAnswer.class, name = "FreeAnswer"),
        @JsonSubTypes.Type(value = MultivaluedQualitativeAnswer.class, name = "MultivaluedQualitativeAnswer"),
        @JsonSubTypes.Type(value = NumericAnswer.class, name = "NumericAnswer"),
        @JsonSubTypes.Type(value = QualitativeAnswer.class, name = "QualitativeAnswer"),
        @JsonSubTypes.Type(value = UnivaluedQualitativeAnswer.class, name = "UnivaluedQualitativeAnswer")})
public abstract class Answer {

    public static final String ANSWERS = "answers.json";
    private String username;
    private Integer questionId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public static Set<Answer> getAnswers() {
        Set<Answer> answers = new HashSet<>();

        File file = new File(ANSWERS);
        if (!file.exists()) return answers;

        ObjectMapper mapper = new ObjectMapper();
        try {
            answers = mapper.readValue(
                    file, mapper.getTypeFactory().constructCollectionType(Set.class, Answer.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (username != null ? !username.equals(answer.username) : answer.username != null) return false;
        return questionId != null ? questionId.equals(answer.questionId) : answer.questionId == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (questionId != null ? questionId.hashCode() : 0);
        return result;
    }

    public abstract void save();
}
