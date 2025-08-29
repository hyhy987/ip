package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.exception.LilBirdException;

/**
 * Represents a command that lists all tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks in the task list.
     * <p>
     * If the list is empty, shows a message stating there are no tasks.
     *
     * @param tasks   Task list to display.
     * @param ui      User interface for showing output.
     * @param storage Storage (unused).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            ui.showBox("No tasks yet.");
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i+1).append(". ").append(tasks.get(i));
                if (i < tasks.size() - 1) sb.append("\n");
            }
            ui.showBox(sb.toString());
        }
    }
}
