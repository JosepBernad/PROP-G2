package survey;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import question.Option;
import question.QualitativeQuestion;
import question.Question;
import user.LogInController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SurveyDetailController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    private JFXListView<Label> surveyInfo;

    @FXML
    private Label surveyTitle;

    @FXML
    private Label surveyDescription;

    private Stage stage;

    @FXML
    public void initialize() {
    }

    public void setSurvey(Integer surveyId) {
        Survey survey = Survey.getSurveyById(surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Question> questions = survey.getQuestions();

        Integer num_p = 0;

        for (Question question : questions) {
            surveyInfo.getItems().add(new Label(++num_p + ". " + question.getStatement()));
            if (question instanceof QualitativeQuestion) {
                Set<Option> options = ((QualitativeQuestion) question).getOptions();
                for (Option option : options) surveyInfo.getItems().add(new Label("         " + option.getValue()));
            }
        }
    }

    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
