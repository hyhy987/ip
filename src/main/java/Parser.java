public class Parser {

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
        default:
            throw new LilBirdException("*soft chirp* I don't recognise that command. Try: list, todo, " +
                    "deadline, event, mark, unmark, delete, bye.");
        }
    }

    public static String commandWord(String full) {
        int sp = full.indexOf(' ');
        return sp == -1 ? full : full.substring(0, sp);
    }

    public static String arguments(String full) {
        int sp = full.indexOf(' ');
        return sp == -1 ? "" : full.substring(sp + 1).trim();
    }

    /** Parses a 1-based index with bounds check against count; returns the 1-based value if valid. */
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
