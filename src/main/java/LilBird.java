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

    public static void main(String[] args) {
        printBox("Hello! I'm LilBird\nWhat can I do for you?");

        //Store and track the tasks
        Task[] tasks = new Task[100];
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
            } else if (!input.isEmpty()) {
                    if (count < tasks.length) {
                        tasks[count] = new Task(input);
                        count++;
                        printBox("added: " + input);
                    } else {
                        printBox("Sorry, task list is full (100).");
                    }
                } else {
                    printBox("*soft chirp* say something?");
                }
            }
        sc.close();
    }
}
