package survey;

import analysis.KMeansController;
import answer.AnswerCreatorController;
import answer.AnswerDetailController;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import exceptions.SurveyAnsweredException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import question.*;
import user.User;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurveyDetailController {

    @FXML
    public Label surveyTitle;

    @FXML
    public Label surveyDescription;

    @FXML
    private VBox questionsBox;

    private Stage stage;
    private Integer surveyId;
    private User user;

    public void init(Stage stage, Integer surveyId, User user) {
        this.stage = stage;
        this.surveyId = surveyId;
        this.user = user;
        Survey survey = Survey.getSurveyById(this.surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Question> questions = new ArrayList<>();
        try {
            questions = Survey.getSurveyById(surveyId).getQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Question question : questions) {
            String statement = question.getStatement();
            questionsBox.getChildren().add(new Label(statement));
            if (question instanceof FreeQuestion) createFreeQuestion((FreeQuestion) question);
            else if (question instanceof NumericQuestion) createNumericQuestion((NumericQuestion) question);
            else if (question instanceof UnsortedQualitativeQuestion)
                createUnsortedQualitativeQuestion((UnsortedQualitativeQuestion) question);
            else if (question instanceof SortedQualitativeQuestion)
                createSortedQualitativeQuestion((SortedQualitativeQuestion) question);
            else if (question instanceof MultivaluedUnsortedQualitativeQuestion)
                createMultivaluedUnsortedQualitativeQuestion((MultivaluedUnsortedQualitativeQuestion) question);
            questionsBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
    }

    private void createMultivaluedUnsortedQualitativeQuestion(MultivaluedUnsortedQualitativeQuestion question) {
        for (Option option : question.getOptions()) {
            JFXCheckBox checkBox = new JFXCheckBox(option.getValue());
            checkBox.setDisable(Boolean.TRUE);
            questionsBox.getChildren().add(checkBox);
        }
    }

    private void createSortedQualitativeQuestion(SortedQualitativeQuestion question) {
        ToggleGroup options = new ToggleGroup();
        for (Option option : question.getOptions()) {
            JFXRadioButton radioButton = new JFXRadioButton(option.getValue() + " #weight: " + option.getWeight());
            radioButton.setToggleGroup(options);
            radioButton.setDisable(Boolean.TRUE);
            questionsBox.getChildren().add(radioButton);
        }
    }

    private void createUnsortedQualitativeQuestion(UnsortedQualitativeQuestion question) {
        ToggleGroup options = new ToggleGroup();
        for (Option option : question.getOptions()) {
            JFXRadioButton radioButton = new JFXRadioButton(option.getValue());
            radioButton.setToggleGroup(options);
            radioButton.setDisable(Boolean.TRUE);
            questionsBox.getChildren().add(radioButton);
        }
    }

    private void createNumericQuestion(NumericQuestion question) {
        questionsBox.getChildren().add(new Label("Accepted range: " + question.getMin() + " <= x <=" + question.getMax()));
    }

    private void createFreeQuestion(FreeQuestion question) {
        questionsBox.getChildren().add(new Label("Max size for the answer: " + question.getMaxSize().toString()));
    }

    public void back() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void analiseSurvey() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/KMeansView.fxml").openStream());
        KMeansController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void showAnswer() {
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/AnswerDetailView.fxml").openStream());
        } catch (IOException ignored) {
        }
        AnswerDetailController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void answerIt() {
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/AnswerCreatorView.fxml").openStream());
        } catch (IOException ignored) {
        }
        AnswerCreatorController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void edit() throws IOException {
        System.out.println("Edit survey with name: " + Survey.getSurveyById(surveyId).getTitle());
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyEditorView.fxml").openStream());
        SurveyEditorController controller = loader.getController();
        controller.init(stage, user, Survey.getSurveyById(surveyId));
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void delete() throws IOException {
        try {
            Survey.delete(surveyId);
            back();
        } catch (SurveyAnsweredException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Answered survey");
            alert.setContentText("Ooops, you are trying to remove a survey that has associated answers and that is a bad idea");

            alert.showAndWait();
        }
    }
}
