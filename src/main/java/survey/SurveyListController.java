package survey;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurveyListController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

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

    public void init() {
        userName.setText("Welcome " + user.getName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) { this.user = user; }

    public void handleMouseClick() throws IOException {
        int index = surveyLabelList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Survey survey = surveyList.get(index);
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/views/SurveyDetailView.fxml").openStream());
            SurveyDetailController controller = loader.getController();
            controller.setStage(stage);
            controller.setUser(user);
            controller.setSurvey(survey.getId());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(STYLE);
            scene.getStylesheets().add(FONTS);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void logOutButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/LogInView.fxml").openStream());
        LogInController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void importSurveysButtonPressed(ActionEvent actionEvent) throws IOException {
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

    public void createSurveyButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyCreatorView.fxml").openStream());
        SurveyCreatorController controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void exportSurveyButtonPressed(ActionEvent actionEvent) {
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
        controller.setStage(stage);
        controller.setUser(user);
        controller.init();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }
}