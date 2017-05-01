package answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exceptions.ResourceNotFoundException;
import question.Question;
import survey.Survey;
import user.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @Type(value = FreeAnswer.class),
        @Type(value = MultivaluedQualitativeAnswer.class),
        @Type(value = NumericAnswer.class),
        @Type(value = QualitativeAnswer.class),
        @Type(value = UnivaluedQualitativeAnswer.class)})

public abstract class Answer {

    public static final String ANSWERS = "answers.json";
    private Integer surveyId;
    private String username;
    private Integer questionId;

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

    public static void saveAnswersInFile(Set<Answer> answers) {
        answers.addAll(getAnswers());
        FileWriter fileWriter = null;
        try {
            File file = new File(ANSWERS);
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

    public static List<Answer> getAnswersByUsernameAndSurveyID(String username, Integer surveyId) throws Exception {
        Survey survey = Survey.getSurveyById(surveyId);
        if (User.getUserByUsername(username) == null) throw new ResourceNotFoundException();
        Set<Answer> answers = getAnswers();
        Set<Integer> questions = new HashSet<>();
        if (survey == null) throw new ResourceNotFoundException();
        for (Question question : survey.getQuestions())
            questions.add(question.getId());

        for (Answer answer : answers)
            if (!answer.getUsername().equals(username) || !questions.contains(answer.getQuestionId()))
                answers.remove(answer);

        return getOrderedById(answers);
    }

    static List<Answer> getOrderedById(Set<Answer> answers) {
        List<Answer> list = new ArrayList<>();
        list.addAll(answers);
        list.sort(Comparator.comparing(Answer::getQuestionId));
        return list;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

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
        saveAnswersInFile(answers);
    }

    public abstract Double calculateDistance(Answer answer);

    public static class AnswerCollection {
        Map<Integer, Map<String, Map<Integer, Answer>>> answers = new HashMap<>();

        public void addAnswer(Answer answer) {
            Integer surveyId = answer.getSurveyId();
            String username = answer.getUsername();
            Integer questionId = answer.getQuestionId();

            if (!answers.containsKey(surveyId)) answers.put(surveyId, new HashMap<>());
            if (!answers.get(surveyId).containsKey(username)) answers.get(surveyId).put(username, new HashMap<>());
            answers.get(surveyId).get(username).put(questionId, answer);
        }

        public Answer getAnswer(Integer surveyId, String username, Integer questionId) {
            return answers.get(surveyId).get(username).get(questionId);
        }

        public Map<String, Map<Integer, Answer>> getAnswersBySurveyId(Integer surveyId) {
            return answers.get(surveyId);
        }

        public Set<Answer> toSet() {
            HashSet<Answer> answers = new HashSet<>();
            for (Map<String, Map<Integer, Answer>> map : this.answers.values())
                for (Map<Integer, Answer> map2 : map.values())
                    answers.addAll(map2.values());

            return answers;
        }
    }
}
