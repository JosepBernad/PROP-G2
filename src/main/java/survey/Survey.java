package survey;


import answer.Answer;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.EmptyRequiredAttributeException;
import exceptions.SurveyAnsweredException;
import question.Question;
import utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Aquesta classe ens permet gestionar una enquesta i tota la seva informació, preguntes que
 la formen...
 Per aquesta classe disposem de sis atributs. El primer es l’identificador de l’enquesta, el
 segon es el títol d’aquesta, el tercer es la descrpció, el quart és un booleà que ens indica si
 una enquesta esta finalitzada o no (valor per defecte false), el cinquè es un altre booleà que
 determina si l’enquesta es visible i l’últim es una list de questions on hi guardem totes les
 opcions que té la survey en qüestió
 */
public class Survey {

    public static final String SURVEYS = "surveys.json";

    private Integer id;
    private String title;
    private String description;
    private Boolean finished;
    private Boolean visible;
    private List<Question> questions;

    public Survey() {
        questions = new ArrayList<>();
    }

    /**
     * Aquest mètode ens retorna en un map amb clau id i valor enquesta totes les enquestes que
     hi ha en el fitxer d’enquestes
     * @return
     */
    public static Map<Integer, Survey> getSurveys() {
        Map<Integer, Survey> surveys = new HashMap<>();

        File file = new File(SURVEYS);
        if (!file.exists()) return surveys;

        ObjectMapper mapper = new ObjectMapper();
        try {
            surveys = mapper.readValue(
                    file, mapper.getTypeFactory().constructMapType(Map.class, Integer.class, Survey.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return surveys;
    }

    /**
     * Aquest mètode ens retorna una enquesta a partir d’un id que se li passa com a paràmetre
     * @param id
     * @return
     */
    public static Survey getSurveyById(Integer id) {
        return getSurveys().get(id);
    }

    /**
     * Aquest mètode esborra una enquesta del fitxer d’enquestes a partir d’un id que se li passa
     com a paràmetre
     * @param id
     */
    public static void delete(Integer id) throws SurveyAnsweredException {
        Map<Integer, Survey> surveys = getSurveys();
        if (!Answer.getAnswersBySurveyId(id).isEmpty()) throw new SurveyAnsweredException();
        surveys.remove(id);
        FileUtils.saveObjectInFile(surveys, SURVEYS);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Amb aquest mètode podem afegir una nova pregunta a l’enquesta
     * @param question
     */
    public void addQuestion(Question question) {
        if (question.getId() == null)
            question.setId(getMaxId(questions));
        questions.add(question);
    }

    /**
     * Amb aquest mètode obtenim un set de Questions amb totes les preguntes que té l’enquesta
     en qüestió
     * @param i
     * @return
     */
    public Question getQuestion(Integer i) {
        return questions.get(i);
    }

    /**
     * Amb aquest mètode guardem tots els valors dels atributs de la classe en el fitxer de
     enquestes
     * @throws EmptyRequiredAttributeException
     */
    public void save() throws EmptyRequiredAttributeException {
        if (title == null || title.length() == 0) throw new EmptyRequiredAttributeException();

        Map<Integer, Survey> surveys = getSurveys();
        if (id == null) {
            if (surveys.isEmpty()) id = 1;
            else id = Collections.max(surveys.keySet()) + 1;
        }
        surveys.put(id, this);
        FileUtils.saveObjectInFile(surveys, SURVEYS);
    }

    /**
     * Aquest mètode retorna l'id maxim donat un conjunt de preguntes
     * @param questions
     * @return
     */
    private int getMaxId(List<Question> questions) {
        return questions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (id != null ? !id.equals(survey.id) : survey.id != null) return false;
        if (title != null ? !title.equals(survey.title) : survey.title != null) return false;
        if (description != null ? !description.equals(survey.description) : survey.description != null) return false;
        if (finished != null ? !finished.equals(survey.finished) : survey.finished != null) return false;
        if (visible != null ? !visible.equals(survey.visible) : survey.visible != null) return false;
        return questions != null ? questions.equals(survey.questions) : survey.questions == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (finished != null ? finished.hashCode() : 0);
        result = 31 * result + (visible != null ? visible.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "id:  " + id + " - title: " + title;
    }

    /**
     *Aquest mètode permet que l'usuari carregui al programa enquestes ja existents en el seu ordinador
     * @param jsonPath
     * @throws FileNotFoundException
     */
    public static void importSurveys(String jsonPath) throws FileNotFoundException {
        Map<Integer, Survey> surveys = new HashMap<>();
        File file = new File(jsonPath);
        if (!file.exists()) throw new FileNotFoundException();
        ObjectMapper mapper = new ObjectMapper();
        try {
            surveys = mapper.readValue(
                    file, mapper.getTypeFactory().constructMapType(Map.class, Integer.class, Survey.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        surveys.putAll(getSurveys());
        FileUtils.saveObjectInFile(surveys, SURVEYS);
    }

    /**
     * Aquest mètode permet al usuari guardar en el seu ordinador totes les enquestes ja existents en el programa
     * @param path
     */
    public static void exportSurveys(String path) {
        FileUtils.saveObjectInFile(getSurveys(),path);
    }
}
