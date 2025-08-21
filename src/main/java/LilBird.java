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

    public static void main(String[] args) {
        printBox("Hello! I'm LilBird\nWhat can I do for you?");

        //Store and track the tasks
        Task[] tasks = new Task[CAP];
        int count = 0;

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                printBox("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                if (count == 0) {
                    printBox("No tasks yet.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < count; i++) {
                        sb.append("\n").append(i + 1).append(". ").append(tasks[i]);
                        if (i < count - 1) sb.append("\n");
                    }
                    printBox(sb.toString());
                }
            } else if (input.startsWith("mark ")){
                int idx = Integer.parseInt(input.split(" ")[1]) - 1;
                tasks[idx].markAsDone();
                printBox("Nice! I've marked this task as done:\n " + tasks[idx]);

            } else if (input.startsWith("unmark ")){
                String[] parts = input.split(" ");
                int idx = Integer.parseInt(parts[1]) - 1;
                tasks[idx].markAsNotDone();
                printBox("OK, I've marked this task as not done yet:\n " + tasks[idx]);

            } else if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                if (desc.isEmpty()) throw new IllegalArgumentException("Todo description cannot be empty.");
                tasks[count++] = new Todo(desc);
                printBox("Got it. I've added this task:\n  " + tasks[count - 1]
                        + "\nNow you have " + count + " tasks in the list.");

            } else if (input.startsWith("deadline ")){
                String rest = input.substring(9).trim();
                int byIdx = rest.indexOf("/by");
                if (byIdx == -1) throw new IllegalArgumentException("Missing '/by'. Usage: deadline <desc> /by <time>");
                String desc = rest.substring(0, byIdx).trim();
                String by = rest.substring(byIdx + 3).trim();
                tasks[count++] = new Deadline(desc, by);
                printBox("Got it. I've added this task:\n  " + tasks[count - 1]
                        + "\nNow you have " + count + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int fromIdx = rest.indexOf("/from");
                int toIdx = rest.indexOf("/to");
                if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx)
                    throw new IllegalArgumentException("Missing '/from' or '/to'. Usage: event <desc> /from <start> /to <end>");
                String desc = rest.substring(0, fromIdx).trim();
                String from = rest.substring(fromIdx + 5, toIdx).trim();
                String to = rest.substring(toIdx + 3).trim();
                tasks[count++] = new Event(desc, from, to);
                printBox("Got it. I've added this task:\n  " + tasks[count - 1]
                        + "\nNow you have " + count + " tasks in the list.");


            } else if (!input.isEmpty()) {
                    if (count < tasks.length) {
                        tasks[count] = new Task(input);
                        count++;
                        printBox("added: " + input);
                    } else {
                        printBox("Sorry, task list is full (" + CAP + ").");
                    }
                } else {
                    printBox("*soft chirp* say something?");
                }
            }
        sc.close();
    }
}
