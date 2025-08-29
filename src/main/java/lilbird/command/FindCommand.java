package lilbird.command;

import lilbird.model.TaskList;
import lilbird.ui.Ui;
import lilbird.storage.Storage;
import lilbird.task.Task;

/**
 * Represents a command that finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {

    /** Raw keyword as typed by the user. */
    private final String keyword;

    /**
     * Creates a FindCommand.
     *
     * @param keyword Keyword to search for (case-insensitive).
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by scanning task descriptions and showing matches.
     *
     * @param tasks   Task list to search.
     * @param ui      User interface to display results.
     * @param storage Storage (unused).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        if (k.isEmpty()) {
            ui.showBox("Please provide a keyword to search for. Example: find book");
            return;
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int shown = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.getDescription().toLowerCase().contains(k)) {
                shown++;
                sb.append(shown).append(". ").append(t);
                if (i < tasks.size() - 1) {
                    sb.append("\n");
                }
            }
        }

        if (shown == 0) {
            ui.showBox("No matching tasks found.");
        } else {
            ui.showBox(sb.toString());
        }
    }
}
