package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.task.Task;
import lilbird.task.Deadline;

public class AddDeadlineCommand extends Command {
    private final String desc;
    private final String byRaw;

    public AddDeadlineCommand(String desc, String byRaw) {
        this.desc = desc;
        this.byRaw = byRaw;
    }

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
