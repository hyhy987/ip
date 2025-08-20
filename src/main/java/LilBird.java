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
        Scanner sc = new Scanner(System.in);
        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                printBox("Bye. Hope to see you again soon!");
                break;
            }
            //Echo whatever user types
            printBox(input);
        }
        sc.close();
    }
}
