package lilbird.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import lilbird.LilBird;

/**
 * Controller for the main GUI window (defined in {@code MainWindow.fxml}).
 * <p>
 * Wires FXML controls, auto-scrolls the dialog container, forwards user input
 * to {@code LilBird}, and renders user/bot dialog bubbles.
 */
public class MainWindow {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private LilBird lilBird;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/User.png"));
    private final Image lilBirdImage = new Image(getClass().getResourceAsStream("/images/LilBird.png"));

    /**
     * Initializes the controller after its FXML fields are injected.
     * Binds the scroll pane to always show the most recent dialog.
     */
    @FXML
    public void initialize() {
        assert scrollPane != null : "FXML: scrollPane not injected";
        assert dialogContainer != null : "FXML: dialogContainer not injected";
        assert userInput != null : "FXML: userInput not injected";
        assert sendButton != null : "FXML: sendButton not injected";
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the LilBird application logic into this controller.
     *
     * @param d the {@code LilBird} instance to use; must not be {@code null}
     */
    public void setLilBird(LilBird d) {
        assert d != null : "LilBird instance must not be null";
        lilBird = d;
    }

    /**
     * Handles user input from the text field / Send button.
     * <p>
     * Creates a user dialog for the typed text, queries LilBird for a response,
     * creates a bot dialog for the reply, appends both to the dialog container,
     * and clears the input field.
     */

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
