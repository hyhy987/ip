package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;

public class UnmarkCommand extends Command {
    private final String indexArg;
    public UnmarkCommand(String indexArg) { this.indexArg = indexArg; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        int idx = Parser.parseIndex1Based(indexArg, tasks.size()) - 1;
        tasks.get(idx).markAsNotDone();
        storage.save(tasks.copy());
        ui.showBox("OK, I've marked this task as not done yet:\n " + tasks.get(idx));
    }
}
