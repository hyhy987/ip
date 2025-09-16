package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.task.Task;
import lilbird.task.Todo;

/**
 * Represents a command that adds a {@link Todo} task to the task list.
 */
public class AddTodoCommand extends Command {
    private final String description;

    /**
     * Creates an AddTodoCommand.
     *
     * @param description Raw description string typed by the user.
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Returns an {@link AddTodoCommand} built from a raw argument string.
     * <p>
     * Expected format:
     * <pre>{@code
     * todo <description>
     * }</pre>
     *
     * <p>Trims the input and validates it is non-empty.
     *
     * @param args the raw arguments after the {@code todo} command word; may be {@code null}.
     * @return a command that will add a {@code Todo} with the given description.
     * @throws LilBirdException if {@code args} is {@code null} or blank.
     */
    public static AddTodoCommand fromArgs(String args) throws LilBirdException {
        String desc = (args == null) ? "" : args.trim();
        if (desc.isEmpty()) {
            throw new LilBirdException("The description of a todo cannot be empty.");
        }
        return new AddTodoCommand(desc);
    }

    /**
     * Executes the add-todo command by creating a new {@link Todo},
     * adding it to the task list, saving the updated list, and showing
     * confirmation to the user.
     * <p>
     * The description must not be empty; otherwise, an exception is thrown.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving updated task list.
     * @throws LilBirdException If the description is empty.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        Task t = new Todo(description);
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
