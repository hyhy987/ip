package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.task.Task;
import lilbird.task.Event;

/**
 * Represents a command that adds an {@link Event} task to the task list.
 */
public class AddEventCommand extends Command {
    private final String desc, fromRaw, toRaw;

    /**
     * Creates an AddEventCommand.
     *
     * @param desc    Description of the event.
     * @param fromRaw Raw string representing the start date/time.
     * @param toRaw   Raw string representing the end date/time.
     */
    public AddEventCommand(String desc, String fromRaw, String toRaw) {
        this.desc = desc;
        this.fromRaw = fromRaw;
        this.toRaw = toRaw;
    }

    /**
     * Executes the add-event command by creating a new {@link Event},
     * adding it to the task list, saving the updated list, and showing
     * confirmation to the user.
     *
     * @param tasks   Task list to operate on.
     * @param ui      User interface for showing feedback.
     * @param storage Storage for saving updated task list.
     * @throws LilBirdException If description, start, or end is empty, or parsing fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        if (desc == null || desc.trim().isEmpty()
                || fromRaw == null || fromRaw.trim().isEmpty()
                || toRaw == null || toRaw.trim().isEmpty()) {
            throw new LilBirdException("Event description, start, and end cannot be empty.");
        }
        Task t = Event.fromUserInput(desc.trim(), fromRaw.trim(), toRaw.trim());
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
