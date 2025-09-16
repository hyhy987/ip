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
     * Returns an {@link AddDeadlineCommand} built from a raw argument string.
     * <p>
     * Expected format:
     * <pre>{@code
     * deadline <description> /by <time>
     * }</pre>
     * where {@code <time>} is later parsed by {@code Deadline.fromUserInput}.
     *
     * <p>Splits on the first occurrence of {@code "/by"}, trims both parts, and validates
     * that neither is empty.
     *
     * @param args the raw arguments after the {@code deadline} command word; may be {@code null}.
     * @return a command that will add a {@code Deadline} with the given description and time.
     * @throws LilBirdException if {@code /by} is missing, or description/time is blank.
     */
    public static AddDeadlineCommand fromArgs(String args) throws LilBirdException {
        int byIdx = (args == null) ? -1 : args.indexOf("/by");
        if (byIdx == -1) {
            throw new LilBirdException("Missing '/by'. Usage: deadline <desc> /by <time>");
        }
        String desc = args.substring(0, byIdx).trim();
        String by   = args.substring(byIdx + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new LilBirdException("Deadline description and time cannot be empty.");
        }
        return new AddDeadlineCommand(desc, by);
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
        Task t = Deadline.fromUserInput(desc, byRaw);
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
