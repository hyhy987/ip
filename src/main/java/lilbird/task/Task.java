package lilbird.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lilbird.exception.LilBirdException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a generic task that can be marked as done or not done.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;
    private final LinkedHashSet<String> tags = new LinkedHashSet<>();

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
            Task t;

            switch (kind) {
            case "T": {
                String desc = unescape(raw[2]);
                t = new Todo(desc);
                if ("1".equals(done)) {
                    t.markAsDone();
                }
                break;
            }
            case "D": {
                String desc = unescape(raw[2]);
                String when = unescape(raw[3]);
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
                break;
            }
            case "E": {
                String desc = unescape(raw[2]).trim();
                LocalDateTime from = LocalDateTime.parse(unescape(raw[3]).trim());
                LocalDateTime to = LocalDateTime.parse(unescape(raw[4]).trim());
                t = new Event(desc, from, to);
                if("1".equals(done)) {
                    t.markAsDone();
                }
                break;
            }
            default:
                throw new LilBirdException("Unknown task type: " + kind);
            }
            int expected = switch (kind) {
                case "T" -> 3;
                case "D" -> 4;
                case "E" -> 5;
                default -> 0;
            };
            if (raw.length > expected) {
                String tagsCsv = unescape(raw[expected].trim());
                if (!tagsCsv.isEmpty()) {
                    t.setTagsFromCsv(tagsCsv);
                }
            }
            return t;
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

    public Set<String> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    public void addTags(Collection<String> rawTags) {
        if (rawTags == null) {
            return;
        }
        for (String s : rawTags) {
            String t = normalizeTag(s);
            if (!t.isEmpty()) {
                tags.add(t);
            }
        }
    }

    public void removeTags(Collection<String> rawTags) {
        if (rawTags == null) {
            return;
        }
        for (String s : rawTags) {
            String t = normalizeTag(s);
            if (!t.isEmpty()) {
                tags.remove(t);
            }
        }
    }

    public boolean hasTag(String raw) {
        String t = normalizeTag(raw);
        return !t.isEmpty() && tags.contains(t);
    }

    protected String formatTagsSuffix() {
        if (tags.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" {");
        boolean first = true;
        for (String t : tags) {
            if (!first) {
                sb.append(' ');
            }
            sb.append('#').append(t);
            first = false;
        }
        return sb.append('}').toString();
    }

    protected String serializeTagsSuffix() {
        if (tags.isEmpty()) {
            return "";
        }
        return " | " + escape(String.join(",", tags));
    }

    protected void setTagsFromCsv(String csv) {
        if (csv == null || csv.isEmpty()) {
            return;
        }
        for (String part : csv.split(",")) {
            String t = normalizeTag(part);
            if (!t.isEmpty()) {
                tags.add(t);
            }
        }
    }

    private static String normalizeTag(String s) {
        if (s == null) {
            return "";
        }
        String t = s.trim();
        if (t.startsWith("#")) {
            t = t.substring(1). trim();
        }
        return t.toLowerCase();
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

    /**
     * Returns the task description
     *
     * @return String task description
     */
    public String getDescription() {
        return this.description;
    }

}