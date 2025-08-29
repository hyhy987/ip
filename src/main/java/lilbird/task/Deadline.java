package lilbird.task;

import lilbird.exception.LilBirdException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a task that has a deadline.
 * <p>
 * A deadline can be specified either as a date only ({@code yyyy-MM-dd}),
 * or as a date and time ({@code yyyy-MM-dd HHmm}).
 */
public class Deadline extends Task {

    private final LocalDate date;         //if user gives only date
    private final LocalDateTime dateTime; //if user gives date + time

    private static final DateTimeFormatter PRINT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter PRINT_DT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Creates a Deadline task with a due date.
     *
     * @param description Description of the task.
     * @param date Date by which the task is due.
     */
    public Deadline(String description, LocalDate date) {
        super(description, TaskType.DEADLINE);
        this.date = Objects.requireNonNull(date);
        this.dateTime = null;
    }

    /**
     * Creates a Deadline task with a due date and time.
     *
     * @param description Description of the task.
     * @param dateTime Date and time by which the task is due.
     */
    public Deadline(String description, LocalDateTime dateTime) {
        super(description, TaskType.DEADLINE);
        this.date = null;
        this.dateTime = Objects.requireNonNull(dateTime);
    }

    /**
     * Creates a Deadline task from raw user input.
     * <p>
     * Accepts formats {@code yyyy-MM-dd} (date only) or {@code yyyy-MM-dd HHmm} (date and time).
     *
     * @param description Description of the task.
     * @param byRaw Raw input string representing the deadline.
     * @return A new Deadline task.
     * @throws LilBirdException If the input format is invalid or cannot be parsed.
     */
    public static Deadline fromUserInput(String description, String byRaw) throws LilBirdException {
        byRaw = byRaw.trim();
        try {
            //if yyyy-MM-dd HHmm
            if (byRaw.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                String[] parts = byRaw.split(" ");
                LocalDate d = LocalDate.parse(parts[0]);
                int hh = Integer.parseInt(parts[1].substring(0, 2));
                int mm = Integer.parseInt(parts[1].substring(2, 4));
                return new Deadline(description, d.atTime(hh, mm));
            } else if (byRaw.matches("\\d{4}-\\d{2}-\\d{2}")) {
                //yyyy-MM-dd
                return new Deadline(description, LocalDate.parse(byRaw));
            } else {
                throw new LilBirdException(
                        "Invalid date format. Use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-10-15 or 2019-10-15 1830)");
            }
        } catch (Exception e) {
            if (e instanceof LilBirdException) {
                throw (LilBirdException) e;
            }
            throw new LilBirdException("Could not parse date/time. " +
                    "Use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-10-15 or 2019-10-15 1830)");
        }
    }

    /**
     * Serializes this Deadline into a storage-friendly string.
     *
     * @return Serialized representation of this Deadline.
     */
    @Override
    public String serialize() {
        String when = (dateTime != null) ? dateTime.toString() : date.toString();
        return String.join(" | ",
                type.getSymbol(),
                isDone ? "1" : "0",
                escape(description),
                escape(when)
        );
    }

    /**
     * Returns the string representation of this Deadline.
     *
     * @return String including description and formatted deadline.
     */
    @Override
    public String toString() {
        String when = (dateTime != null) ? dateTime.format(PRINT_DT) : date.format(PRINT_DATE);
        return super.toString() + " (by: " + when + ")";
    }
}