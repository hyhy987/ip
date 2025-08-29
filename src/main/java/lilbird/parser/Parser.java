package lilbird.parser;

import lilbird.command.*;
import lilbird.exception.LilBirdException;

/**
 * Parses raw user input into executable commands.
 */
public class Parser {

    /**
     * Parses the full command string into a Command object.
     *
     * @param fullCommand Raw input string from the user.
     * @return Command representing the parsed action.
     * @throws LilBirdException If the command is invalid.
     */
    public static Command parse(String fullCommand) throws LilBirdException {
        if (fullCommand == null) throw new LilBirdException("Empty command.");
        String trimmed = fullCommand.trim();
        if (trimmed.isEmpty()) throw new LilBirdException("Empty command.");

        String cmd = commandWord(trimmed);
        String args = arguments(trimmed);

        switch (cmd) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "todo":
            return new AddTodoCommand(args);
        case "deadline": {
            // format: deadline <desc> /by <time>
            int byIdx = args.indexOf("/by");
            if (byIdx == -1) throw new LilBirdException("Missing '/by'. Usage: deadline <desc> /by <time>");
            String desc = args.substring(0, byIdx).trim();
            String by = args.substring(byIdx + 3).trim();
            return new AddDeadlineCommand(desc, by);
        }
        case "event": {
            // format: event <desc> /from <start> /to <end>
            int fromIdx = args.indexOf("/from");
            int toIdx = args.indexOf("/to");
            if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                throw new LilBirdException("Missing '/from' or '/to'. Usage: event <desc> /from <start> /to <end>");
            }
            String desc = args.substring(0, fromIdx).trim();
            String from = args.substring(fromIdx + 5, toIdx).trim();
            String to = args.substring(toIdx + 3).trim();
            return new AddEventCommand(desc, from, to);
        }
        case "mark":
            return new MarkCommand(args);
        case "unmark":
            return new UnmarkCommand(args);
        case "delete":
            return new DeleteCommand(args);
        case "find":
            return new FindCommand(args);
        default:
            throw new LilBirdException("*soft chirp* I don't recognise that command. Try: list, todo, " +
                    "deadline, event, mark, unmark, delete, bye.");
        }
    }

    /**
     * Extracts the command word (the first token) from a full command string.
     * <p>
     * For example, given {@code "todo read book"}, returns {@code "todo"}.
     *
     * @param full Full user input string.
     * @return Command word; the entire string if no space is present.
     */
    public static String commandWord(String full) {
        int sp = full.indexOf(' ');
        return sp == -1 ? full : full.substring(0, sp);
    }

    /**
     * Extracts the argument portion from a full command string.
     * <p>
     * For example, given {@code "todo read book"}, returns {@code "read book"}.
     *
     * @param full Full user input string.
     * @return Arguments after the first space, trimmed; empty string if none.
     */
    public static String arguments(String full) {
        int sp = full.indexOf(' ');
        return sp == -1 ? "" : full.substring(sp + 1).trim();
    }

    /**
     * Parses a 1-based index string provided by the user.
     * <p>
     * Ensures the index is within the range {@code 1..count}.
     *
     * @param arg   String representing the 1-based index.
     * @param count Total number of available items.
     * @return Parsed index as an integer (1-based).
     * @throws LilBirdException If the input is not numeric, empty, or out of range.
     */
    public static int parseIndex1Based(String arg, int count) throws LilBirdException {
        try {
            String trimmed = arg.trim();
            if (trimmed.isEmpty()) throw new NumberFormatException();
            int idx1 = Integer.parseInt(trimmed);
            if (idx1 < 1 || idx1 > count) throw new LilBirdException("That task number doesn’t exist.");
            return idx1;
        } catch (NumberFormatException e) {
            throw new LilBirdException("Please provide a valid task number.");
        }
    }
}
