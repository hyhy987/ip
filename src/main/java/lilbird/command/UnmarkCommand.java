package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;

/**
 * Represents a command that marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private final String indexArg;

    /**
     * Creates an UnmarkCommand.
     *
     * @param indexArg User-provided argument representing a 1-based index.
     */
    public UnmarkCommand(String indexArg) {
        this.indexArg = indexArg;
    }

    /**
     * Executes the unmark command by updating the specified task’s
     * status to not done, saving the updated list, and showing
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
        tasks.get(idx).markAsNotDone();
        storage.save(tasks.copy());
        ui.showBox("OK, I've marked this task as not done yet:\n " + tasks.get(idx));
    }
}
