package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;
import lilbird.task.Task;

public class DeleteCommand extends Command {
    private final String indexArg;
    public DeleteCommand(String indexArg) { this.indexArg = indexArg; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        int idx = Parser.parseIndex1Based(indexArg, tasks.size()) - 1;
        Task removed = tasks.removeAt(idx); // your TaskList has remove(int)
        storage.save(tasks.copy());
        ui.showBox("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
