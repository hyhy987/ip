package lilbird.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lilbird.exception.LilBirdException;

/**
 * Represents a generic task that can be marked as done or not done.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Converts this task into a serialized form for storage.
     *
     * @return Serialized string representation.
     */
    public String serialize() {
        return String.join(" | ",
                type.getSymbol(),
                isDone ? "1" : "0",
                escape(description)
        );
    }

    /**
     * Deserializes a task from a line in the save file.
     *
     * @param line Line containing serialized task.
     * @return Task represented by the line.
     * @throws LilBirdException If the line is corrupted or invalid.
     */
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

    /**
     * Returns a status icon representing whether this task is done.
     *
     * @return {@code "X"} if the task is marked done, otherwise a space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); //mark done task with X
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Escapes a string so it can be safely serialized.
     * <p>
     * The pipe character {@code |} is replaced with {@code \|}.
     * Null values are converted to an empty string.
     *
     * @param s String to escape, may be {@code null}.
     * @return Escaped string safe for storage.
     */
    protected static String escape(String s) {
        return Objects.toString(s, "").replace("|", "\\|");
    }

    /**
     * Reverses {@link #escape(String)} by unescaping pipe characters.
     * <p>
     * Each occurrence of {@code \|} is converted back to {@code |}.
     *
     * @param s Escaped string.
     * @return Original string with pipe characters restored.
     */
    protected static String unescape(String s) {
        return s.replace("\\|", "|");
    }

    /**
     * Returns a human-readable string representation of the task.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "]" + description;
    }


}