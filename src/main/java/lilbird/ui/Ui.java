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
}
