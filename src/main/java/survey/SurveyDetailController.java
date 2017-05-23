package survey;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import question.Option;
import question.QualitativeQuestion;
import question.Question;

import java.util.List;
import java.util.Set;

public class SurveyDetailController {

    @FXML
    private JFXListView<Label> surveyInfo;
    private Stage stage;
    private Integer surveyId;

    @FXML
    public void initialize() {
        Survey survey = Survey.getSurveyById(surveyId);
        surveyInfo.getItems().add(new Label(survey.getTitle()));
        surveyInfo.getItems().add(new Label(survey.getDescription()));

        List<Question> questions = survey.getQuestions();

        Integer num_p = 0;

        for (Question question : questions) {
            surveyInfo.getItems().add(new Label( ++num_p + ". " + question.getStatement()));
            if (question instanceof QualitativeQuestion) {
                Set<Option> options = ((QualitativeQuestion) question).getOptions();
                for (Option option : options) surveyInfo.getItems().add(new Label("         " + option.getValue()));
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }
}
