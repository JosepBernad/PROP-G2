package analysis;

import answer.Answer;
import answer.AnswerController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KMeansController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    public JFXComboBox<Label> numberOfClusters;

    @FXML
    public HBox clustersPane;

    @FXML
    public HBox chart;

    @FXML
    public Label distance;
    private Integer surveyId;
    private Stage stage;

    @FXML
    private Label errorText;

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
        if (numberOfClusters.getValue() == null) errorText.setText("Select the number of clusters");
        else {
            clustersPane.getChildren().clear();
            errorText.setText("");
            KMeans kMeans = new KMeans(surveyId);
            int k = Integer.parseInt(numberOfClusters.getSelectionModel().getSelectedItem().getText());
            List<Cluster> calc = kMeans.calc(k);
            for (Cluster cluster : calc) {
                VBox vBox = new VBox(5);
                vBox.setAlignment(Pos.CENTER);
                JFXListView<String> list = new JFXListView<>();
                list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    showAnswer(newValue);
                });
                for (UserPoint userPoint : cluster.getPoints()) {
                    list.getItems().add(userPoint.getUsername());
                    list.setPrefSize(200, 150);
                }
                vBox.getChildren().addAll(list, new JFXButton("Centroid"));
                clustersPane.getChildren().add(vBox);
            }

            JFreeChart individualsChart = createIndividualsChart(calc);
            ChartPanel individualsPanel = new ChartPanel(individualsChart);
            SwingNode individualsNode = new SwingNode();
            individualsNode.setContent(individualsPanel);

            JFreeChart clustersChart = createClustersChart(calc);
            ChartPanel clustersPanel = new ChartPanel(clustersChart);
            SwingNode clustersNode = new SwingNode();
            clustersNode.setContent(clustersPanel);

            chart.getChildren().clear();
            chart.getChildren().addAll(individualsNode, clustersNode);
        }
    }

    private void showAnswer(String username) {
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/AnswerView.fxml").openStream());
        } catch (IOException ignored) {
        }
        AnswerController controller = loader.getController();
        controller.init(surveyId, username);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }


    private JFreeChart createIndividualsChart(List<Cluster> calc) {
        List<Double> values = new ArrayList<>();
        calc.forEach(cluster -> cluster.getPoints().forEach(userPoint -> values.add(userPoint.getDistanceToCentroid())));

        double[] array = new double[values.size()];
        for (int i = 0; i < values.size(); i++) array[i] = values.get(i);

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Histogram", array, values.size(), 0, 1);

        JFreeChart histogram = ChartFactory.createHistogram("Individuals intra distances histogram",
                "Distances", "Observations", dataset, PlotOrientation.VERTICAL,
                false, false, false);
        NumberAxis rangeAxis = (NumberAxis) histogram.getXYPlot().getRangeAxis();
        rangeAxis.setRange(0, values.size());

        histogram.setBorderVisible(true);
        histogram.setBorderPaint(Color.BLACK);

        XYPlot categoryPlot = histogram.getXYPlot();
        XYBarRenderer renderer = (XYBarRenderer) categoryPlot.getRenderer();
        renderer.setMargin(0.1);

        return histogram;
    }

    private JFreeChart createClustersChart(List<Cluster> calc) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Integer i = 1;
        Double totalDistance = 0D;
        for (Cluster c : calc) {
            double sum = 0D;
            for (UserPoint point : c.getPoints())
                sum += point.getDistanceToCentroid();
            totalDistance += sum;
            dataset.addValue(sum / c.getPoints().size(), "Value", "Cluster " + i);
            ++i;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        distance.setText("Total distance: " + df.format(totalDistance));

        JFreeChart barChart = ChartFactory.createBarChart("Cluster intra distances",
                "Clusters", "Mean distances", dataset, PlotOrientation.VERTICAL,
                false, false, false);

        NumberAxis rangeAxis = (NumberAxis) barChart.getCategoryPlot().getRangeAxis();
        rangeAxis.setRange(0, 1);

        barChart.setBorderVisible(true);
        barChart.setBorderPaint(Color.BLACK);

        return barChart;
    }
}
