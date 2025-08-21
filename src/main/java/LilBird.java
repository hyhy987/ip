import java.util.Scanner;

public class LilBird {
    private static final String LINE = "____________________________________________________________";
    private static final int CAP = 100;

    private static void printBox(String message) {
        System.out.println("    " + LINE);
        //to support multi-line messages
        for (String line : message.split("\\R")) {
            System.out.println("    " + line);
        }
        System.out.println("    " + LINE);
    }

    //Command Handlers
    private static void handleList(Task[] tasks, int count) {
        if (count == 0) {
            printBox("No tasks yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < count; i++) {
                sb.append(i+1).append(". ").append(tasks[i]);
                if (i < count - 1) sb.append("\n");
            }
            printBox(sb.toString());
        }
    }

    private static void handleMark(String input, Task[] tasks, int count) throws LilBirdException {
        int idx = parseIndex(input, count);
        tasks[idx].markAsDone();
        printBox("Nice! I've marked this task as done:\n " + tasks[idx]);
    }

    private static void handleUnmark(String input, Task[] tasks, int count) throws LilBirdException {
        int idx = parseIndex(input, count);
        tasks[idx].markAsNotDone();
        printBox("OK, I've marked this task as not done yet:\n " + tasks[idx]);
    }

    private static void handleTodo(String input, Task[] tasks, int count) throws LilBirdException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new LilBirdException("The description of a todo cannot be empty.");
        tasks[count] = new Todo(desc);
        printBox("Got it. I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static void handleDeadline(String input, Task[] tasks, int count) throws LilBirdException {
        String rest = input.length() > 8 ? input.substring(9).trim() : "";
        int byIdx = rest.indexOf("/by");
        if (byIdx == -1) throw new LilBirdException("Missing '/by'. Usage: deadline <desc> /by <time>");
        String desc = rest.substring(0, byIdx).trim();
        String by = rest.substring(byIdx + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) throw new LilBirdException("Deadline description and time cannot be empty.");
        tasks[count] = new Deadline(desc, by);
        printBox("Got it. I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static void handleEvent(String input, Task[] tasks, int count) throws LilBirdException {
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
        tasks[count] = new Event(desc, from, to);
        printBox("Got it. I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static void handleAdd(String input, Task[] tasks, int count) throws LilBirdException {
        if (count >= CAP) throw new LilBirdException("Sorry, task list is full (" + CAP + ").");
        tasks[count] = new Task(input);
        printBox("added: " + input);
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

        Task[] tasks = new Task[CAP];
        int count = 0;

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    printBox("Bye. Hope to see you again soon!");
                    break;
                } else if (input.equals("list")) {
                    handleList(tasks, count);
                } else if (input.startsWith("mark ")) {
                    handleMark(input, tasks, count);
                } else if (input.startsWith("unmark ")) {
                    handleUnmark(input, tasks, count);
                } else if (input.startsWith("todo")) {
                    handleTodo(input, tasks, count);
                    count++;
                } else if (input.startsWith("deadline")) {
                    handleDeadline(input, tasks, count);
                    count++;
                } else if (input.startsWith("event")) {
                    handleEvent(input, tasks, count);
                    count++;
                } else if (!input.isEmpty()) {
                    handleAdd(input, tasks, count);
                    count++;
                } else {
                    throw new LilBirdException("*soft chirp* say something?");
                }

            } catch (LilBirdException e) {
                printBox("OOPS!!! " + e.getMessage());
            }
        }
        sc.close();
    }
}
