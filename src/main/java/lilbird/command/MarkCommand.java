package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;

/**
 * Represents a command that marks a task as done.
 */
public class MarkCommand extends Command {
    private final String indexArg;//1-based as typed by user

    /**
     * Creates a MarkCommand.
     *
     * @param indexArg User-provided argument representing a 1-based index.
     */
    public MarkCommand(String indexArg) {
        this.indexArg = indexArg;
    }

    /**
     * Executes the mark command by updating the specified task’s
     * status to done, saving the updated list, and showing
     * confirmation to the user.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving updated task list.
     * @throws LilBirdException If the index is invalid or saving fails.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        int idx = Parser.parseIndex1Based(indexArg, tasks.size()) - 1;
        tasks.get(idx).markAsDone();
        storage.save(tasks.copy());
        ui.showBox("Nice! I've marked this task as done:\n " + tasks.get(idx));
    }
}
