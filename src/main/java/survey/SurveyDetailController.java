package survey;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import question.Option;
import question.QualitativeQuestion;
import question.Question;

import java.util.List;
import java.util.Set;

public class SurveyDetailController {

    @FXML
    private JFXListView<Label> surveyInfo;

    public void initialize() {
        Survey survey = Survey.getSurveyById(6);
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
}
