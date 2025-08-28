public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final java.util.Scanner sc = new java.util.Scanner(System.in);

    public void showLine() {
        System.out.println(LINE);
    }

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

    public String readCommand() {
        return sc.nextLine();
    }

    public boolean hasNextLine() {
        return sc.hasNextLine();
    }

    public void close() {
        sc.close();
    }



}
