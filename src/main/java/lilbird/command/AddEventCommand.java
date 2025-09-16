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
     * Returns an {@link AddEventCommand} built from a raw argument string.
     * <p>
     * Expected format:
     * <pre>{@code
     * event <description> /from <start> /to <end>
     * }</pre>
     * where {@code <start>} and {@code <end>} are later parsed by {@code Event.fromUserInput}.
     *
     * <p>Locates {@code "/from"} and {@code "/to"} (in that order), extracts and trims the three
     * segments, and validates that none is empty.
     *
     * @param args the raw arguments after the {@code event} command word; may be {@code null}.
     * @return a command that will add an {@code Event} with the given description and time range.
     * @throws LilBirdException if {@code /from} or {@code /to} is missing or out of order,
     *                          or if any segment is blank.
     */
    public static AddEventCommand fromArgs(String args) throws LilBirdException {
        if (args == null) {
            throw new LilBirdException("Missing arguments.");
        }
        int fromIdx = args.indexOf("/from");
        int toIdx   = args.indexOf("/to");
        if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
            throw new LilBirdException("Missing '/from' or '/to'. Usage: event <desc> /from <start> /to <end>");
        }
        String desc = args.substring(0, fromIdx).trim();
        String from = args.substring(fromIdx + 5, toIdx).trim();
        String to   = args.substring(toIdx + 3).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new LilBirdException("Event description, start, and end cannot be empty.");
        }
        return new AddEventCommand(desc, from, to);
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
        Task t = Event.fromUserInput(desc, fromRaw, toRaw);
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
