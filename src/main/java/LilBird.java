import java.util.ArrayList;

public class LilBird {
    private final Ui ui;

    public LilBird() {
        this.ui = new Ui();
    }

    //Command Handlers
    private void handleList(TaskList taskList) {
        if (taskList.isEmpty()) {
            ui.showBox("No taskList yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < taskList.size(); i++) {
                sb.append(i+1).append(". ").append(taskList.get(i));
                if (i < taskList.size() - 1) sb.append("\n");
            }
            ui.showBox(sb.toString());
        }
    }

    private void handleMark(String input, TaskList taskList, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, taskList.size());
        taskList.get(idx).markAsDone();
        storage.save(taskList.copy());
        ui.showBox("Nice! I've marked this task as done:\n " + taskList.get(idx));
    }

    private void handleUnmark(String input, TaskList taskList, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, taskList.size());
        taskList.get(idx).markAsNotDone();
        storage.save(taskList.copy());
        ui.showBox("OK, I've marked this task as not done yet:\n " + taskList.get(idx));
    }

    private void handleTodo(String input, TaskList taskList, Storage storage) throws LilBirdException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new LilBirdException("The description of a todo cannot be empty.");
        Task t = new Todo(desc);
        taskList.add(t);
        storage.save(taskList.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + taskList.size() + " taskList in the list.");
    }

    private void handleDeadline(String input, TaskList taskList, Storage storage) throws LilBirdException {
        String rest = input.length() > 8 ? input.substring(9).trim() : "";
        int byIdx = rest.indexOf("/by");
        if (byIdx == -1) throw new LilBirdException("Missing '/by'. Usage: deadline <desc> /by <time>");
        String desc = rest.substring(0, byIdx).trim();
        String by = rest.substring(byIdx + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) throw new LilBirdException("Deadline description and time cannot be empty.");
        Task t = Deadline.fromUserInput(desc, by);
        taskList.add(t);
        storage.save(taskList.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + taskList.size() + " taskList in the list.");
    }

    private void handleEvent(String input, TaskList taskList, Storage storage) throws LilBirdException {
        String rest = input.length() > 5 ? input.substring(6).trim() : "";
        int fromIdx = rest.indexOf("/from");
        int toIdx = rest.indexOf("/to");
        if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx)
            throw new LilBirdException("Missing '/from' or '/to'. Usage: event <desc> /from <start> /to <end>");
        String desc = rest.substring(0, fromIdx).trim();
        String from = rest.substring(fromIdx + 5, toIdx).trim();
        String to = rest.substring(toIdx + 3).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty())
            throw new LilBirdException("Event description, start, and end cannot be empty.");
        Task t = Event.fromUserInput(desc, from, to);
        taskList.add(t);
        storage.save(taskList.copy());
        ui.showBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + taskList.size() + " taskList in the list.");
    }

    private void handleDelete(String input, TaskList taskList, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, taskList.size());
        Task removed = taskList.removeAt(idx);
        storage.save(taskList.copy());
        ui.showBox("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + taskList.size() + " taskList in the list.");
    }

    // === Utility ===
    private static int parseIndex(String input, int count) throws LilBirdException {
        try {
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
            if (idx < 0 || idx >= count) throw new LilBirdException("That task number doesn’t exist.");
            return idx;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new LilBirdException("Please provide a valid task number.");
        }
    }

    public void run() {
        ui.showBox("Hello! I'm LilBird\nWhat can I do for you?");

        Storage storage = new Storage("data/lilbird.txt");
        TaskList taskList;

        try {
            taskList = new TaskList(new ArrayList<>(storage.load()));
        } catch (LilBirdException e) {
            taskList = new TaskList();
            ui.showBox("OOPS!!! " + e.getMessage() + "\nStarting with an empty list.");
        }


        while (true) {
            if (!ui.hasNextLine()) break;
            String input = ui.readCommand().trim();

            try {
                if (input.equals("bye")) {
                    ui.showBox("Bye. Hope to see you again soon!");
                    break;
                } else if (input.equals("list")) {
                    handleList(taskList);
                } else if (input.startsWith("mark ")) {
                    handleMark(input, taskList, storage);
                } else if (input.startsWith("unmark ")) {
                    handleUnmark(input, taskList, storage);
                } else if (input.startsWith("todo")) {
                    handleTodo(input, taskList, storage);
                } else if (input.startsWith("deadline")) {
                    handleDeadline(input, taskList, storage);
                } else if (input.startsWith("event")) {
                    handleEvent(input, taskList, storage);
                } else if (input.startsWith("delete")) {
                    handleDelete(input, taskList, storage);
                }
                else {
                    throw new LilBirdException("*soft chirp* \"I don't recognise that command. Try: list, todo, " +
                            "deadline, event, mark, unmark, delete, bye.\"");
                }

            } catch (LilBirdException e) {
                ui.showBox("OOPS!!! " + e.getMessage());
            }
        }
        ui.close();
    }


    public static void main(String[] args) {
        new LilBird().run();
    }
}
