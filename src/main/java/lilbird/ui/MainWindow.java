package lilbird.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import lilbird.LilBird;

/** Controller for the main GUI. */
public class MainWindow {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private LilBird lilBird;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/User.png"));
    private final Image lilBirdImage = new Image(getClass().getResourceAsStream("/images/LilBird.png"));

    @FXML
    public void initialize() {
        assert scrollPane != null : "FXML: scrollPane not injected";
        assert dialogContainer != null : "FXML: dialogContainer not injected";
        assert userInput != null : "FXML: userInput not injected";
        assert sendButton != null : "FXML: sendButton not injected";
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the LilBird instance. */
    public void setLilBird(LilBird d) {
        assert d != null : "LilBird instance must not be null";
        lilBird = d;
    }

    /** Handles pressing Enter or clicking Send. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = lilBird.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLilBirdDialog(response, lilBirdImage)
        );
        userInput.clear();
    }
}
