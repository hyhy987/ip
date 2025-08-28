public class ListCommand extends Command {
    
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
