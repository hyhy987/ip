package lilbird.task;

import lilbird.exception.LilBirdException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a task that spans a period of time.
 * <p>
 * An event has a start date/time and an end date/time, both given
 * in the format {@code yyyy-MM-dd HHmm}.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    private static final DateTimeFormatter PRINT_DT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Creates an Event task with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Start date/time of the event.
     * @param to End date/time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    /**
     * Creates an Event from raw user input.
     * <p>
     * Both {@code fromRaw} and {@code toRaw} must be in the format {@code yyyy-MM-dd HHmm}.
     *
     * @param description Description of the event.
     * @param fromRaw Raw string representing the start date/time.
     * @param toRaw Raw string representing the end date/time.
     * @return A new Event task.
     * @throws LilBirdException If the inputs are invalid or cannot be parsed.
     */
    public static Event fromUserInput(String description, String fromRaw, String toRaw) throws LilBirdException {
        try {
            if (!fromRaw.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}") ||
                !toRaw.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                throw new LilBirdException("Use yyyy-MM-dd HHmm for events, e.g., 2025-09-07 1400");
            }
            String[] pf = fromRaw.split(" ");
            String[] pt = toRaw.split(" ");
            LocalDateTime f = LocalDate.parse(pf[0]).atTime(
                Integer.parseInt(pf[1].substring(0, 2)), Integer.parseInt(pf[1].substring(2,4)));
            LocalDateTime t = LocalDate.parse(pt[0]).atTime(
                    Integer.parseInt(pt[1].substring(0,2)), Integer.parseInt(pt[1].substring(2,4)));
            return new Event(description, f, t);
        } catch (Exception e) {
            throw new LilBirdException("Could not parse event times. Use yyyy-MM-dd HHmm for both start and end.");
        }
    }

    /**
     * Serializes this Event into a storage-friendly string.
     *
     * @return Serialized representation of this Event.
     */
    @Override
    public String serialize() {
        return String.join(" | ",
                type.getSymbol(),
                isDone ? "1" : "0",
                escape(description),
                escape(from.toString()),
                escape(to.toString())
        );
    }

    /**
     * Returns the string representation of this Event.
     *
     * @return String including description, start, and end times.
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + from.format(PRINT_DT)
                + " to: " + to.format(PRINT_DT) + ")";
    }
}
