package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;
import lilbird.parser.Parser;

public class MarkCommand extends Command {
    private final String indexArg;//1-based as typed by user

    public MarkCommand(String indexArg) {
        this.indexArg = indexArg;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        int idx = Parser.parseIndex1Based(indexArg, tasks.size()) - 1;
        tasks.get(idx).markAsDone();
        storage.save(tasks.copy());
        ui.showBox("Nice! I've marked this task as done:\n " + tasks.get(idx));
    }
}
