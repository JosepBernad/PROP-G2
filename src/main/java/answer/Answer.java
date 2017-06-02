package answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ResourceNotFoundException;
import question.Question;
import survey.Survey;
import user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *Aquesta classe ens permet gestionar la resposta d’un usuari a una pregunta d’una enquesta determinada
 *Per aquesta classe disposem de dos atributs, el username que ens indica l’usuari que ha
 respòs a la pregunta i el quest questionId ionId que ens indica a quina enquesta pertany la
 resposta de l’usuari.
 */

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

    /**
     * Amb aquest mètode obtenim un set de respostes amb totes les respostes que hi ha
     guardades al fitxer de respostes
     * @return un set de answers
     */
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

    /**
     * Aquest mètode ens permet guardar totes les respostes al fitxer de respostes
     * @param answers són les respostes
     */
    public static void saveAnswersInFile(Set<Answer> answers) {
        answers.addAll(getAnswers());
        FileWriter fileWriter = null;
        try {
            File file = new File(ANSWERS);
            fileWriter = new FileWriter(file);
            if (!file.exists()) file.createNewFile();
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructCollectionType(Set.class, Answer.class);
            fileWriter.write(mapper.writerWithDefaultPrettyPrinter().forType(type).writeValueAsString(answers));
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

    /**
     * Amb aquest mètode obtenim totes les respostes (en una llista d’elles) d’una enquesta
     determinada que ha contestat un usuari en concret
     * @param username és el nom de l'usuari
     * @param surveyId és l'id de l'enquesta
     * @return una llista d'answers
     * @throws Exception excepció
     */
    public static List<Answer> getAnswersByUsernameAndSurveyID(String username, Integer surveyId) throws Exception {
        Survey survey = Survey.getSurveyById(surveyId);
        if (User.getUserByUsername(username) == null) throw new ResourceNotFoundException();
        Set<Answer> answers = getAnswers();
        Set<Integer> questions = new HashSet<>();
        if (survey == null) throw new ResourceNotFoundException();
        for (Question question : survey.getQuestions())
            questions.add(question.getId());

        Set<Answer> userAnswers = new HashSet<>();

        for (Answer answer : answers)
            if (answer.getUsername().equals(username) && questions.contains(answer.getQuestionId()))
                userAnswers.add(answer);

        return getOrderedById(userAnswers);
    }

    /**
     * Aquest mètode ens ordena una llista de respostes en funció al identificador (id) de la seva
     pregunta, ordenant-ho així de menor a major
     * @param answers són les respostes
     * @return una llista d'answers
     */
    static List<Answer> getOrderedById(Set<Answer> answers) {
        List<Answer> list = new ArrayList<>();
        list.addAll(answers);
        list.sort(Comparator.comparing(Answer::getQuestionId));
        return list;
    }

    /**
     * Aquest mètode retorna totes les respostes de l'usuari amb un determinat id a una enquesta
     * @param surveyId és l'id de l'enquesta
     * @return un map de maps de valors, answers i valors enters
     */
    public static Map<String, Map<Integer, Answer>> getAnswersBySurveyId(Integer surveyId) {
        Map<String, Map<Integer, Answer>> map = new HashMap<>();

        Set<Answer> answers = getAnswers();

        for (Answer answer : answers) {
            if (answer.getSurveyId().equals(surveyId)) {
                String username = answer.getUsername();
                Integer questionId = answer.getQuestionId();
                if (!map.containsKey(username)) map.put(username, new HashMap<>());
                map.get(username).put(questionId, answer);
            }
        }

        return map;
    }

    /**
     * Aquest mètode permet importar respostes a una determinada enquesta existent
     * @param jsonPath és el path
     * @throws FileNotFoundException excepció
     */
    public static void importAnswers(String jsonPath) throws FileNotFoundException {
        Set<Answer> answers = new HashSet<>();

        File file = new File(jsonPath);

        if(!file.exists()) throw new FileNotFoundException();

        ObjectMapper mapper = new ObjectMapper();
        try {
            answers = mapper.readValue(
                    file, mapper.getTypeFactory().constructCollectionType(Set.class, Answer.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveAnswersInFile(answers);
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

    /**
     * Aquest metode retorna la distància (entre 0 i 1) entre una resposta i una altre resposta del
     mateix tipus
     * @param answer són les respostes
     * @return una answer
     */
    public abstract Double calculateDistance(Answer answer);

}
