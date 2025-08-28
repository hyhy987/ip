package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException;

    public boolean isExit() {
        return false;
    }
}
