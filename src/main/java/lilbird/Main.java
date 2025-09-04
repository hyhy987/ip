package lilbird;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lilbird.LilBird;
import lilbird.ui.MainWindow;

/** A GUI for LilBird using FXML. */
public class Main extends Application {
    private final LilBird lilBird = new LilBird();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxml = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxml.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("LilBird");
            fxml.<MainWindow>getController().setLilBird(lilBird); // inject chatbot
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
