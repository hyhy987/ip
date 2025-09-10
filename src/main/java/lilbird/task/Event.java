package lilbird.task;

import lilbird.exception.LilBirdException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a time-bounded event with a start and end {@link LocalDateTime}.
 */
public class Event extends Task {

    private final LocalDateTime from;
    private final LocalDateTime to;

    // Input format (what the user types)
    private static final DateTimeFormatter IN_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Output format (what we display back)
    private static final DateTimeFormatter PRINT_DT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    private static final String ERR_FORMAT =
            "Use yyyy-MM-dd HHmm for events, e.g., 2025-09-07 1400";

    /**
     * Creates an event occurring between two date-times (inclusive start, exclusive end).
     *
     * @param description task description
     * @param from        start date-time (non-null)
     * @param to          end date-time (non-null, not before {@code from})
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(Objects.requireNonNull(description, "description"), TaskType.EVENT);
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        assert !this.to.isBefore(this.from) : "Event end must not be before start";
    }

    /**
     * Parses user input and constructs an {@code Event}.
     * Expected format for both endpoints: {@code yyyy-MM-dd HHmm}.
     *
     * @param description task description
     * @param fromRaw     raw string after {@code /from}
     * @param toRaw       raw string after {@code /to}
     * @return constructed event
     * @throws LilBirdException if inputs cannot be parsed or end is before start
     */
    public static Event fromUserInput(String description, String fromRaw, String toRaw) throws LilBirdException {
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(fromRaw, "fromRaw");
        Objects.requireNonNull(toRaw, "toRaw");

        try {
            LocalDateTime f = LocalDateTime.parse(fromRaw.trim(), IN_DT);
            LocalDateTime t = LocalDateTime.parse(toRaw.trim(), IN_DT);
            if (t.isBefore(f)) {
                throw new LilBirdException("End cannot be before start.");
            }
            return new Event(description, f, t);
        } catch (LilBirdException e) {
            throw e; // keep specific message
        } catch (Exception e) {
            throw new LilBirdException(ERR_FORMAT);
        }
    }

    @Override
    public String serialize() {
        return String.join(" | ",
                type.getSymbol(),
                isDone ? "1" : "0",
                escape(description),
                escape(from.toString()),
                escape(to.toString())
        ) + serializeTagsSuffix();
    }

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + from.format(PRINT_DT)
                + " to: " + to.format(PRINT_DT) + ")"
                + formatTagsSuffix();
    }
}
