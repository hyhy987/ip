import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /** Base serializer: T|D|E, done flag, description. Subclasses append extra fields. */
    public String serialize() {
        return String.join(" | ",
                type.getSymbol(),
                isDone ? "1" : "0",
                escape(description)
        );
    }

    public static Task deserialize(String line) throws LilBirdException {
        try {
            String[] raw = line.split("\\| ", -1);
            String kind = raw[0].trim();
            String done = raw[1].trim();

            switch (kind) {
            case "T": {
                String desc = unescape(raw[2]);
                Task t = new Todo(desc);
                if ("1".equals(done)) t.markAsDone();
                return t;
            }
            case "D": {
                String desc = unescape(raw[2]);
                String when = unescape(raw[3]);
                Task t;
                if (when.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                    t = new Deadline(desc, LocalDateTime.parse(when));
                } else if (when.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    t= new Deadline(desc, LocalDate.parse(when));
                } else {
                    throw new LilBirdException("Corrupted deadline date: " + when);
                }
                if ("1".equals(done)) {
                    t.markAsDone();
                }
                return t;
            }
            case "E": {
                String desc = unescape(raw[2]).trim();
                LocalDateTime from = LocalDateTime.parse(unescape(raw[3]).trim());
                LocalDateTime to = LocalDateTime.parse(unescape(raw[4]).trim());
                Task t = new Event(desc, from, to);
                if("1".equals(done)) {
                    t.markAsDone();
                }
                return t;
            }
            default:
                throw new LilBirdException("Unknown task type: " + kind);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new LilBirdException("Corrupted save line: " + line);
        }
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); //mark done task with X
    }

    //Marks task as done
    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    protected static String escape(String s) {
        return Objects.toString(s, "").replace("|", "\\|");
    }

    protected static String unescape(String s) {
        return s.replace("\\|", "|");
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "]" + description;
    }


}