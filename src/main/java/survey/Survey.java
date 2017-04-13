package survey;


import com.fasterxml.jackson.databind.ObjectMapper;
import question.Question;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Survey {

    public static final String SURVEY = "surveys.json";

    private Integer id;
    private String title;
    private String description;
    private Boolean finished;
    private Boolean visible;
    private Set<Question> questions;

    public Survey() {
        questions = new HashSet<>();
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

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void save() {
        Map<Integer, Survey> surveys = getSurveys();
        surveys.put(id, this);

        File file = new File(SURVEY);
        try {
            if (!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new ObjectMapper().writeValueAsString(surveys));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Survey> getSurveys() {
        Map<Integer, Survey> surveys = new HashMap<>();

        File file = new File(SURVEY);
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

    public static Survey getSurveyById(Integer id) {
        return getSurveys().get(id);
    }
}
