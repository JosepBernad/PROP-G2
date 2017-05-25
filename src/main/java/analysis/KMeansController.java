package analysis;

import answer.Answer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class KMeansController {

    @FXML
    public JFXComboBox<Label> numberOfClusters;

    @FXML
    public HBox clustersPane;

    @FXML
    private Label surveyTitle;

    private Integer surveyId;

    private Stage stage;

    @FXML
    public void initialize() {
    }

    public void goodInitialize() {
        Map<String, Map<Integer, Answer>> answers = Answer.getAnswersBySurveyId(surveyId);
        for (Integer i = 1; i <= answers.keySet().size(); ++i)
            numberOfClusters.getItems().add(new Label(i.toString()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public void doKmeans() {
        clustersPane.getChildren().clear();
        KMeans kMeans = new KMeans(surveyId, 4);
        int k = Integer.parseInt(numberOfClusters.getSelectionModel().getSelectedItem().getText());
        List<Cluster> calc = kMeans.calc(k);
        for (Cluster cluster : calc) {
            JFXListView<String> list = new JFXListView<>();
            for (UserPoint userPoint : cluster.getPoints()) {
                list.getItems().add(userPoint.getUsername());
                list.setPrefSize(200, 100);
            }
            clustersPane.getChildren().add(list);
        }
    }
}
