import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import user.LogInControllerView;
import utils.Constants;

import java.io.IOException;


public class Main extends Application {

    private static final String APP_TITLE = "Enquestator 1.0";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/LogInView.fxml"));
        Parent parent = loader.load();
        LogInControllerView controller = loader.getController();
        controller.init(primaryStage);
        Scene scene = new Scene(parent);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toExternalForm()));
        primaryStage.show();
    }

}