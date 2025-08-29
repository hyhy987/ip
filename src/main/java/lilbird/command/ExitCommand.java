package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;

/**
 * Represents a command that exits the application.
 */
public class ExitCommand extends Command {

    /**
     * Executes the exit command by showing a goodbye message.
     *
     * @param tasks   Task list (unused).
     * @param ui      User interface for showing feedback.
     * @param storage Storage (unused).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBox("Bye. Hope to see you again soon!");
    }

    /**
     * Returns true because this command signals the application should exit.
     *
     * @return True.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
