package lilbird.ui;

import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A chat dialog bubble consisting of a text label and an avatar image.
 * <p>
 * This class uses the {@code fx:root} pattern and is backed by
 * {@code DialogBox.fxml}. Factory methods create user or bot variants.
 */
public class DialogBox extends HBox {
    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert dialog != null && displayPicture != null : "FXML fields not injected";
        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog so that the avatar appears on the left and the text on the right.
     * Used for LilBird (bot) messages.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Returns a dialog bubble representing a user message.
     *
     * @param text message text to display
     * @param img  avatar image to show on the right
     * @return a new dialog bubble for the user message
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Returns a dialog bubble representing a LilBird (bot) message.
     * The bubble is flipped so the avatar appears on the left.
     *
     * @param text message text to display
     * @param img  avatar image to show on the left
     * @return a new dialog bubble for the bot message
     */
    public static DialogBox getLilBirdDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}
