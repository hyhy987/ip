package lilbird.task;

import lilbird.exception.LilBirdException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a task that has a deadline (date only or date+time).
 * <p>
 * Exactly one of {@code date} or {@code dateTime} is set.
 */
public class Deadline extends Task {

    private final LocalDate date;         // if user gives only date
    private final LocalDateTime dateTime; // if user gives date + time

    // Input formats (what the user types)
    private static final DateTimeFormatter IN_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter IN_DT   = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Output formats (what we display back)
    private static final DateTimeFormatter PRINT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter PRINT_DT   = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    private static final String ERR_FORMAT =
            "Invalid date format. Use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-10-15 or 2019-10-15 1830)";

    /**
     * Creates a deadline with a date (no time).
     *
     * @param description description of the task
     * @param date        deadline date (non-null)
     */
    public Deadline(String description, LocalDate date) {
        super(Objects.requireNonNull(description, "description"), TaskType.DEADLINE);
        this.date = Objects.requireNonNull(date, "date");
        this.dateTime = null;
        assert (this.date != null) ^ (this.dateTime != null)
                : "Exactly one of date or dateTime must be set";
    }

    /**
     * Creates a deadline with a specific date and time.
     *
     * @param description description of the task
     * @param dateTime    deadline date-time (non-null)
     */
    public Deadline(String description, LocalDateTime dateTime) {
        super(Objects.requireNonNull(description, "description"), TaskType.DEADLINE);
        this.date = null;
        this.dateTime = Objects.requireNonNull(dateTime, "dateTime");
        assert (this.date != null) ^ (this.dateTime != null)
                : "Exactly one of date or dateTime must be set";
    }

    /**
     * Parses user input and constructs a {@code Deadline}.
     * <ul>
     *   <li>{@code yyyy-MM-dd} → date only</li>
     *   <li>{@code yyyy-MM-dd HHmm} → date + time</li>
     * </ul>
     *
     * @param description task description
     * @param byRaw       raw string after {@code /by}
     * @return constructed deadline
     * @throws LilBirdException if the input cannot be parsed
     */
    public static Deadline fromUserInput(String description, String byRaw) throws LilBirdException {
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(byRaw, "byRaw");
        String s = byRaw.trim();

        try {
            if (s.length() == 10) { // yyyy-MM-dd
                return new Deadline(description, LocalDate.parse(s, IN_DATE));
            }
            if (s.length() == 16) { // yyyy-MM-dd HHmm
                return new Deadline(description, LocalDateTime.parse(s, IN_DT));
            }
            throw new LilBirdException(ERR_FORMAT);
        } catch (Exception e) {
            throw new LilBirdException(ERR_FORMAT);
        }
    }

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

    @Override
    public String toString() {
        String when = (dateTime != null)
                ? dateTime.format(PRINT_DT)
                : date.format(PRINT_DATE);
        return super.toString() + " (by: " + when + ")";
    }
}
