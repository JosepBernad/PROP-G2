package answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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

    public void save() {
        Set<Answer> answers = getAnswers();
        answers.add(this);
        saveAnswersInFile(answers, ANSWERS);
    }

    public static void saveAnswersInFile(Set<Answer> answers, String filename) {
        answers.addAll(getAnswers());
        FileWriter fileWriter = null;
        try {
            File file = new File(filename);
            fileWriter = new FileWriter(file);
            if (!file.exists()) file.createNewFile();
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructCollectionType(Set.class, Answer.class);
            fileWriter.write(mapper.enable(SerializationFeature.INDENT_OUTPUT).writerFor(type).writeValueAsString(answers));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Answer> getAnswersByUsernameAndSurveyID(String username, Integer surveyId)
    {
        return null;
    }

    public abstract Double calculateDistance(Answer answer);
}
