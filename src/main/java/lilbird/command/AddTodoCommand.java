package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.task.Task;
import lilbird.task.Todo;

public class AddTodoCommand extends Command {
    private final String description;
    public AddTodoCommand(String description) {
        this.description = description;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        String desc = description.length() > 4 ? description.substring(5).trim() : "";
        if (desc.isEmpty()) throw new LilBirdException("The description of a todo cannot be empty.");
        Task t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " taskList in the list.");
    }
}
