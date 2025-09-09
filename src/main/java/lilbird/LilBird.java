package lilbird;

import lilbird.command.ExitCommand;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.model.TaskList;
import lilbird.parser.Parser;
import lilbird.command.Command;
import lilbird.exception.LilBirdException;
import java.util.ArrayList;

/**
 * Entry point of the LilBird application.
 * Wires together the UI, storage, and task list, then runs the command loop.
 */
public class LilBird {
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructs a LilBird instance and loads tasks from the default save file.
     * If loading fails, starts with an empty task list.
     */
    public LilBird() {
        this.storage = new Storage("data/lilbird.txt");
        TaskList loaded;
        try {
            loaded = new TaskList(new ArrayList<>((storage.load())));
        } catch (LilBirdException e) {
            loaded = new TaskList();
        }
        this.tasks = loaded;
    }

    public String getResponse(String input) {
        BufferingUi ui = new BufferingUi();
        String trimmed = input == null ? "" : input.trim();

        try {
            if (trimmed.equalsIgnoreCase("bye")) {
                new ExitCommand().execute(tasks, ui, storage);
            } else {
                Command c = Parser.parse(trimmed);
                c.execute(tasks, ui, storage);
            }
        } catch (LilBirdException e) {
            ui.showBox("OOPS!!! " + e.getMessage());
        }
        return ui.getLast();
    }

    /**
     * Minimal {@code Ui} that captures the last message instead of printing.
     * Used by the GUI path to retrieve messages as strings.
     */
    private static class BufferingUi extends Ui {
        private String last = "";

        @Override
        public void showBox(String message) {
            // don't print; just capture for the GUI to read
            this.last = message == null ? "" : message;
        }

        String getLast() {
            return last;
        }
    }

}
