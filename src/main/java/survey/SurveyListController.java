package survey;

import answer.Answer;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user.LogInController;
import user.MyAccountController;
import user.User;
import utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurveyListController {

    @FXML
    private JFXListView<Label> surveyLabelList;

    @FXML
    private Label userName;

    private List<Survey> surveyList;
    private Stage stage;
    private User user;

    @FXML
    public void initialize() {
        surveyList = new ArrayList<>();
        for (Survey survey : Survey.getSurveys().values()) {
            surveyLabelList.getItems().add(new Label(survey.getTitle()));
            surveyList.add(survey);
        }
    }

    public void init(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        userName.setText("Welcome " + user.getName());
    }

    public void handleMouseClick() throws IOException {
        int index = surveyLabelList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Survey survey = surveyList.get(index);
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/views/SurveyDetailView.fxml").openStream());
            SurveyDetailController controller = loader.getController();
            controller.init(stage, survey.getId(), user);
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void logOutButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/LogInView.fxml").openStream());
        LogInController controller = loader.getController();
        controller.init(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void importSurveysButtonPressed() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import surveys");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("json", "*.json*")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                Survey.importSurveys(file.getPath());
                initialize();
            }
        }
    }

    public void createSurveyButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyCreatorView.fxml").openStream());
        SurveyCreatorController controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void exportSurveyButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export surveys");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {
            if(!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".json");
            }
            Survey.exportSurveys(file.getPath());
        }
    }

    public void myAccountButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/MyAccountView.fxml").openStream());
        MyAccountController controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void importAnswersButtonPressed() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import answers");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("json", "*.json*")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null)
            for (File file : list)
                Answer.importAnswers(file.getPath());
    }
}