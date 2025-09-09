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
        String desc = description.length() > 4 ? description.substring(5).trim() : "";
        if (desc.isEmpty()) {
            throw new LilBirdException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " taskList in the list.");
    }
}
