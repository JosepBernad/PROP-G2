package survey;

import analysis.KMeansController;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import question.Option;
import question.QualitativeQuestion;
import question.Question;
import user.User;

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
    private User user;
    private Integer surveyId;

    @FXML
    public void initialize() {
    }

    public void setSurvey(Integer surveyId) {
        this.surveyId = surveyId;
        Survey survey = Survey.getSurveyById(this.surveyId);
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

    public void backButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        controller.init();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) { this.user = user; }

    public void analiseSurvey() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/KMeansView.fxml").openStream());
        KMeansController controller = loader.getController();
        controller.init(stage, surveyId);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }
}
