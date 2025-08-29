package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;
import lilbird.task.Task;

/**
 * Represents a command that deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final String indexArg;

    /**
     * Creates a DeleteCommand.
     *
     * @param indexArg User-provided argument representing a 1-based index.
     */
    public DeleteCommand(String indexArg) {
        this.indexArg = indexArg;
    }

    /**
     * Executes the delete command by removing the specified task
     * from the task list, saving the updated list, and showing
     * confirmation to the user.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving updated task list.
     * @throws LilBirdException If the index is invalid or saving fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        int idx = Parser.parseIndex1Based(indexArg, tasks.size()) - 1;
        Task removed = tasks.removeAt(idx); // your TaskList has remove(int)
        storage.save(tasks.copy());
        ui.showBox("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
