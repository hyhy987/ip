public class AddEventCommand extends Command {
    private final String desc, fromRaw, toRaw;

    public AddEventCommand(String desc, String fromRaw, String toRaw) {
        this.desc = desc;
        this.fromRaw = fromRaw;
        this.toRaw = toRaw;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LilBirdException {
        if (desc == null || desc.trim().isEmpty()
                || fromRaw == null || fromRaw.trim().isEmpty()
                || toRaw == null || toRaw.trim().isEmpty()) {
            throw new LilBirdException("Event description, start, and end cannot be empty.");
        }
        Task t = Event.fromUserInput(desc.trim(), fromRaw.trim(), toRaw.trim());
        tasks.add(t);
        storage.save(tasks.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }
}
