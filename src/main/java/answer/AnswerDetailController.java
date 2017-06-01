package answer;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import survey.Survey;
import survey.SurveyDetailController;
import user.User;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDetailController {

    @FXML
    public Label surveyTitle;

    @FXML
    public Label surveyDescription;

    @FXML
    public VBox answers;

    @FXML
    public JFXButton backButton;

    private Integer surveyId;
    private User user;
    private Stage stage;

    public void init(Integer surveyId, User user) {
        this.surveyId = surveyId;
        this.user = user;
        Survey survey = Survey.getSurveyById(this.surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Answer> answersForUser = new ArrayList<>();
        try {
            answersForUser = Answer.getAnswersByUsernameAndSurveyID(this.user.getUsername(), surveyId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Answer answer : answersForUser) {
            String statement = survey.getQuestion(answer.getQuestionId()).getStatement();
            answers.getChildren().add(new Label(statement));
            if (answer instanceof FreeAnswer) createFreeAnswer((FreeAnswer) answer);
            else if (answer instanceof NumericAnswer) createNumericAnswer((NumericAnswer) answer);
            else if (answer instanceof UnivaluedQualitativeAnswer)
                createUnivaluedQualitativeAnswer((UnivaluedQualitativeAnswer) answer);
            else if (answer instanceof MultivaluedQualitativeAnswer)
                createMultivaluedQualitativeAnswer((MultivaluedQualitativeAnswer) answer);
            answers.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
    }

    public void init(Stage stage, Integer surveyId, User user) {
        this.stage = stage;
        backButton.setVisible(Boolean.TRUE);
        init(surveyId, user);
    }

    private void createMultivaluedQualitativeAnswer(MultivaluedQualitativeAnswer answer) {
        answer.getOptions().forEach(option -> answers.getChildren().add(new Label(option.getValue())));
    }

    private void createUnivaluedQualitativeAnswer(UnivaluedQualitativeAnswer answer) {
        answers.getChildren().add(new Label(answer.getOption().getValue()));
    }

    private void createNumericAnswer(NumericAnswer answer) {
        answers.getChildren().add(new Label(answer.getValue().toString()));
    }

    private void createFreeAnswer(FreeAnswer answer) {
        answers.getChildren().add(new Label(answer.getValue()));
    }

    public void backButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyDetailView.fxml").openStream());
        SurveyDetailController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }
}
