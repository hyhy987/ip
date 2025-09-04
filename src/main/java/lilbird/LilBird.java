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
     * Creates a new instance of the LilBird application.
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


    /**
     * Executes the main program loop:
     * <ol>
     *   <li>Loads tasks from storage.</li>
     *   <li>Reads commands via {@link lilbird.ui.Ui}.</li>
     *   <li>Parses them via {@link lilbird.parser.Parser}.</li>
     *   <li>Executes a {@link lilbird.command.Command}.</li>
     * </ol>
     */
//    public void run() {
//        ui.showBox("Hello! I'm LilBird\nWhat can I do for you?");
//
//        Storage storage = new Storage("data/lilbird.txt");
//        TaskList taskList;
//
//        try {
//            taskList = new TaskList(new ArrayList<>(storage.load()));
//        } catch (LilBirdException e) {
//            taskList = new TaskList();
//            ui.showBox("OOPS!!! " + e.getMessage() + "\nStarting with an empty list.");
//        }
//
//
//        while (true) {
//            if (!ui.hasNextLine()) break;
//            String input = ui.readCommand();
//
//            try {
//                Command c = Parser.parse(input);
//                c.execute(taskList, ui, storage);
//                if (c.isExit()) break;
//            } catch (LilBirdException e) {
//                ui.showBox("OOPS!!! " + e.getMessage());
//            }
//        }
//        ui.close();
//    }

    public String getResponse(String input) {
        BufferingUi ui = new BufferingUi();
        String trimmed = input == null ? "" : input.trim();

        try {
            if (trimmed.equalsIgnoreCase("bye")) {
                new ExitCommand().execute(tasks, ui, storage);
            } else {
                Command c = Parser.parse(trimmed);
                c.execute(tasks, ui,storage);
            }
        } catch (LilBirdException e) {
            ui.showBox("OOPS!!! " + e.getMessage());
        }
        return ui.getLast();
    }

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

    /**
     * Application entry point.
     *
     * @param args Command line arguments (unused).
     */
//    public static void main(String[] args) {
//        new LilBird().run();
//    }
}
