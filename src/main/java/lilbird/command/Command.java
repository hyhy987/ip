package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;

/**
 * Represents an abstract user command in the LilBird application.
 * <p>
 * Concrete subclasses (e.g., {@link DeleteCommand}, {@link ExitCommand})
 * define specific behaviors to be executed against the task list,
 * storage, and user interface.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving/loading tasks.
     * @throws LilBirdException If execution fails due to invalid input or I/O errors.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException;

    /**
     * Returns whether this command signals the application should exit.
     *
     * @return True if this is an exit command; false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
