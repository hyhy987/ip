import java.util.ArrayList;
import java.util.Scanner;

public class LilBird {
    private static final String LINE = "____________________________________________________________";

    private static void printBox(String message) {
        System.out.println("    " + LINE);
        //to support multi-line messages
        for (String line : message.split("\\R")) {
            System.out.println("    " + line);
        }
        System.out.println("    " + LINE);
    }

    //Command Handlers
    private static void handleList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            printBox("No tasks yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i+1).append(". ").append(tasks.get(i));
                if (i < tasks.size() - 1) sb.append("\n");
            }
            printBox(sb.toString());
        }
    }

    private static void handleMark(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, tasks.size());
        tasks.get(idx).markAsDone();
        storage.save(tasks);
        printBox("Nice! I've marked this task as done:\n " + tasks.get(idx));
    }

    private static void handleUnmark(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, tasks.size());
        tasks.get(idx).markAsNotDone();
        storage.save(tasks);
        printBox("OK, I've marked this task as not done yet:\n " + tasks.get(idx));
    }

    private static void handleTodo(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new LilBirdException("The description of a todo cannot be empty.");
        Task t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks);
        printBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleDeadline(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
        String rest = input.length() > 8 ? input.substring(9).trim() : "";
        int byIdx = rest.indexOf("/by");
        if (byIdx == -1) throw new LilBirdException("Missing '/by'. Usage: deadline <desc> /by <time>");
        String desc = rest.substring(0, byIdx).trim();
        String by = rest.substring(byIdx + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) throw new LilBirdException("Deadline description and time cannot be empty.");
        Task t = Deadline.fromUserInput(desc, by);
        tasks.add(t);
        storage.save(tasks);
        printBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleEvent(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
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
        tasks.add(t);
        storage.save(tasks);
        printBox("Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleDelete(String input, ArrayList<Task> tasks, Storage storage) throws LilBirdException {
        int idx = parseIndex(input, tasks.size());
        Task removed = tasks.remove(idx);
        storage.save(tasks);
        printBox("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
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

    public static void main(String[] args) {
        printBox("Hello! I'm LilBird\nWhat can I do for you?");

        Storage storage = new Storage("data/lilbird.txt");
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            tasks = new ArrayList<>(storage.load());
        } catch (LilBirdException e) {
            tasks = new ArrayList<>();
            printBox("OOPS!!! " + e.getMessage() + "\nStarting with an empty list.");
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    printBox("Bye. Hope to see you again soon!");
                    break;
                } else if (input.equals("list")) {
                    handleList(tasks);
                } else if (input.startsWith("mark ")) {
                    handleMark(input, tasks, storage);
                } else if (input.startsWith("unmark ")) {
                    handleUnmark(input, tasks, storage);
                } else if (input.startsWith("todo")) {
                    handleTodo(input, tasks, storage);
                } else if (input.startsWith("deadline")) {
                    handleDeadline(input, tasks, storage);
                } else if (input.startsWith("event")) {
                    handleEvent(input, tasks, storage);
                } else if (input.startsWith("delete")) {
                    handleDelete(input, tasks, storage);
                }
                else {
                    throw new LilBirdException("*soft chirp* \"I don't recognise that command. Try: list, todo, " +
                            "deadline, event, mark, unmark, delete, bye.\"");
                }

            } catch (LilBirdException e) {
                printBox("OOPS!!! " + e.getMessage());
            }
        }
        sc.close();
    }
}
