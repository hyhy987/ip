import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    private static final DateTimeFormatter PRINT_DT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

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

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + from.format(PRINT_DT)
                + " to: " + to.format(PRINT_DT) + ")";
    }
}
