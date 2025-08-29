package lilbird.ui;

import java.util.Scanner;

/**
 * Handles user interaction by reading input and printing formatted output.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the given message in a decorative box.
     *
     * @param msg Text to display
     */
    public void showBox(String msg) {
        showLine();
        for (String line : msg.split("\\R")) {
            System.out.println("    " + line);
        }
        showLine();
    }

    public void showWelcome() {
        showBox("Hello! I'm LilBird\nWhat can I do for you?");
    }
    public void showGoodbye() {
        showBox("Bye. Hope to see you again soon!");
    }
    public void showError(String msg) {
        showBox("☹ OOPS!!! " + msg);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Raw command string entered by the user.
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Returns whether there is another line of input.
     *
     * @return True if another line of input exists.
     */
    public boolean hasNextLine() {
        return sc.hasNextLine();
    }

    /**
     * Closes the input scanner and releases system resources.
     */
    public void close() {
        sc.close();
    }



}
