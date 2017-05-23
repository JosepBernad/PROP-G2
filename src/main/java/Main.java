import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.LogInController;

import java.io.IOException;


public class Main extends Application {

    private static final String APP_TITLE = "Enquestator 1.0";
    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/LogInView.fxml"));
        Parent parent = loader.load();
        LogInController controller = loader.getController();
        controller.setStage(primaryStage);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }

}