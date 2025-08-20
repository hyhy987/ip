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
        private String[] tasks = new String[100];
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
                    printBox("");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < count; i++) {
                        sb.append(i+1).append(". ").append(tasks[i]);
                        if (i < count - 1) sb.append("\n");
                    }
                    printBox(sb.toString());
                }
            }
            //Echo whatever user types
            printBox(input);
        }
        sc.close();
    }
}
