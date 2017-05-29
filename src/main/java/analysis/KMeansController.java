package analysis;

import answer.Answer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import java.util.ArrayList;
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

        JFreeChart plot = createChart(calc);

        ChartPanel panel = new ChartPanel(plot);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(panel);
        chart.getChildren().clear();
        chart.getChildren().add(swingNode);
    }


    private JFreeChart createChart(List<Cluster> calc) {
        List<Double> values = new ArrayList<>();
        for (Cluster c : calc) {
            for (UserPoint point : c.getPoints()) {
                values.add(point.getDistanceToCentroid());
            }
        }

        double[] array = new double[values.size()];
        int i = 0;
        for (Double d : values) {
            array[i] = d;
            ++i;
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Histogram", array, values.size(), 0, 1);

        JFreeChart histogram = ChartFactory.createHistogram("Individuals intra distances histogram",
                "Distances", "Observations", dataset, PlotOrientation.VERTICAL,
                false, false, false);
        NumberAxis rangeAxis = (NumberAxis) histogram.getXYPlot().getRangeAxis();
        rangeAxis.setRange(0, 4);

        return histogram;
    }
}
