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
import user.User;
import utils.Constants;

import java.awt.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KMeansController {

    @FXML
    public JFXComboBox<Label> numberOfClusters;

    @FXML
    public HBox clustersPane;

    @FXML
    public HBox chart;

    @FXML
    public Label distance;

    @FXML
    private Label errorText;

    private Integer surveyId;
    private Stage stage;

    public void init(Stage stage, Integer surveyId) {
        this.stage = stage;
        this.surveyId = surveyId;
        Map<String, Map<Integer, Answer>> answers = Answer.getAnswersBySurveyId(surveyId);
        for (Integer i = 1; i <= answers.keySet().size(); ++i)
            numberOfClusters.getItems().add(new Label(i.toString()));
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
                vBox.setPrefSize(200, 180);
                vBox.setAlignment(Pos.CENTER);
                JFXListView<String> list = new JFXListView<>();
                list.setOnMouseClicked(event ->
                        showAnswer(list.getSelectionModel().getSelectedItem()));
                for (UserPoint userPoint : cluster.getPoints()) {
                    list.getItems().add(userPoint.getUsername());
                    list.setPrefSize(200, 150);
                }
                JFXButton clusterButton = new JFXButton("Show cluster details");
                if (cluster.getPoints().isEmpty()) clusterButton.setDisable(Boolean.TRUE);
                else clusterButton.setOnAction(event -> showClusterDetail(cluster));
                vBox.getChildren().addAll(list, clusterButton);
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

    private void showClusterDetail(Cluster cluster) {
        Point centroid = cluster.getCentroid();
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/ClusterView.fxml").openStream());
        } catch (IOException ignored) {
        }
        ClusterController controller = loader.getController();

        List<List<Answer>> clusterAnswers = new ArrayList<>();

        List<Answer> centroidAnswers = new ArrayList<>();
        for (int i = 0; i < centroid.getNumOfCoordinates(); ++i)
            centroidAnswers.add(centroid.getCoordinate(i));

        clusterAnswers.add(centroidAnswers);

        for (UserPoint userPoint : cluster.getPoints()) {
            List<Answer> answers = new ArrayList<>();
            for (int i = 0; i < userPoint.getNumOfCoordinates(); ++i)
                answers.add(userPoint.getCoordinate(i));
            clusterAnswers.add(answers);
        }

        controller.init(surveyId, clusterAnswers);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        Stage clusterStage = new Stage();
        clusterStage.setTitle("Cluster detail");
        clusterStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/images/icon.png").toExternalForm()));
        clusterStage.setScene(scene);
        clusterStage.show();
    }

    private void showAnswer(String username) {
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/AnswerView.fxml").openStream());
        } catch (IOException ignored) {
        }
        AnswerController controller = loader.getController();
        controller.init(surveyId, User.getUserByUsername(username));
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        Stage answerStage = new Stage();
        answerStage.setTitle("Answer detail");
        answerStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/images/icon.png").toExternalForm()));
        answerStage.setScene(scene);
        answerStage.show();
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
