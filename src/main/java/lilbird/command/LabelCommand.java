package lilbird.command;

import lilbird.exception.LilBirdException;
import lilbird.model.TaskList;
import lilbird.parser.Parser;
import lilbird.storage.Storage;
import lilbird.task.Task;
import lilbird.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LabelCommand extends Command {
    private final String args;
    public LabelCommand(String args) {
        if (args == null) {
            this.args = "";
        } else {
            this.args = args.trim();
        }
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        if (args.isEmpty()) {
            throw new LilBirdException("Usage: label <index> <tag...>");
        }
        StringTokenizer st = new StringTokenizer(args);
        if (!st.hasMoreTokens()) {
            throw new LilBirdException("Missing task index.");
        }
        String idxArg = st.nextToken();

        List<String> tagList = new ArrayList<>();
        while (st.hasMoreTokens()) {
            tagList.add(st.nextToken());
        }
        if (tagList.isEmpty()) {
            throw new LilBirdException("Provide at least one tag.");
        }

        int idx0 = Parser.parseIndex1Based(idxArg, tasks.size()) -  1;
        Task t = tasks.get(idx0);
        t.addTags(tagList);
        storage.save(tasks.copy());
        ui.showBox("Tagged task:\n  " + t);
    }


}
