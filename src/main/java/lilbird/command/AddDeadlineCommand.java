package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.task.Task;
import lilbird.task.Deadline;

/**
 * Represents a command that adds a {@link Deadline} task to the task list.
 */
public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String byRaw;

    /**
     * Creates an AddDeadlineCommand.
     *
     * @param desc  Description of the deadline task.
     * @param byRaw Raw string representing the due date/time.
     */
    public AddDeadlineCommand(String desc, String byRaw) {
        this.desc = desc;
        this.byRaw = byRaw;
    }

    /**
     * Executes the add-deadline command by creating a new {@link Deadline},
     * adding it to the task list, saving the updated list, and showing
     * confirmation to the user.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving updated task list.
     * @throws LilBirdException If description or time is empty, or parsing fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        if (desc == null || desc.trim().isEmpty() || byRaw == null || byRaw.trim().isEmpty()) {
            throw new LilBirdException("Deadline description and time cannot be empty.");
        }
        Task t = Deadline.fromUserInput(desc.trim(), byRaw.trim());
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
