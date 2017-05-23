package survey;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurveyListController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    private JFXListView<Label> surveyLabelList;

    private List<Survey> surveyList;
    private Stage stage;


    @FXML
    public void initialize() {
        surveyList = new ArrayList<>();
        for (Survey survey : Survey.getSurveys().values()) {
            surveyLabelList.getItems().add(new Label(survey.getTitle()));
            surveyList.add(survey);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleMouseClick() throws IOException {
        int index = surveyLabelList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Survey survey = surveyList.get(index);
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/views/SurveyDetailView.fxml").openStream());
            SurveyDetailController controller = loader.getController();
            controller.setStage(stage);
            controller.setSurvey(survey.getId());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(STYLE);
            scene.getStylesheets().add(FONTS);
            stage.setScene(scene);
            stage.show();
        }
    }
}
