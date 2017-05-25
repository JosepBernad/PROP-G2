package analysis;

import answer.Answer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class KMeansController {

    @FXML
    public JFXComboBox<Label> numberOfClusters;

    @FXML
    public HBox clustersPane;

    @FXML
    public Pane chart;

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
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            JFXListView<String> list = new JFXListView<>();
            for (UserPoint userPoint : cluster.getPoints()) {
                list.getItems().add(userPoint.getUsername());
                list.setPrefSize(200, 100);
            }
            vBox.getChildren().addAll(list, new JFXButton("Centroid"));
            clustersPane.getChildren().add(vBox);
        }

        ScatterChart<Number, Number> plot = createChart("Browser Usage Statistics");
        chart.getChildren().add(plot);
    }


    private ScatterChart<Number, Number> createChart(String chartTitle) {
        // Create the X-Axis
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Year");
        // Customize the X-Axis, so points are scattered uniformly
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(1900);
        xAxis.setUpperBound(2300);
        xAxis.setTickUnit(50);

        // Create the Y-Axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Population (in millions)");

        // Create the ScatterChart
        ScatterChart<Number,Number> chart = new ScatterChart<>(xAxis, yAxis);
        // Set the Title for the Chart
        chart.setTitle(chartTitle);
        // Set the Data for the Chart
        ObservableList<XYChart.Series<Number,Number>> chartData = getCountrySeries();
        chart.setData(chartData);
        return chart;
    }

    private ObservableList<XYChart.Series<Number, Number>> getCountrySeries()
    {
        XYChart.Series<Number, Number> seriesChina = new XYChart.Series<Number, Number>();
        seriesChina.setName("China");
        seriesChina.getData().add(new XYChart.Data<Number, Number>(1950, 555));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2000, 1275));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2050, 1395));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2100, 1182));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2150, 1149));

        XYChart.Series<Number, Number> seriesIndia = new XYChart.Series<Number, Number>();
        seriesIndia.setName("India");
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(1950, 358));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2000, 1017));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2050, 1531));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2100, 1458));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2150, 1308));

        XYChart.Series<Number, Number> seriesUSA = new XYChart.Series<Number, Number>();
        seriesUSA.setName("USA");
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(1950, 158));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2000, 285));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2050, 409));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2100, 437));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2150, 453));

        ObservableList<XYChart.Series<Number, Number>> data =
                FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
        data.add(seriesChina);
        data.add(seriesIndia);
        data.add(seriesUSA);
        return data;
    }

}
