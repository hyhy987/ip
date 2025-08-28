import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Deadline extends Task {

    private final LocalDate date;         //if user gives only date
    private final LocalDateTime dateTime; //if user gives date + time

    private static final DateTimeFormatter PRINT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter PRINT_DT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Deadline(String description, LocalDate date) {
        super(description, TaskType.DEADLINE);
        this.date = Objects.requireNonNull(date);
        this.dateTime = null;
    }

    public Deadline(String description, LocalDateTime dateTime) {
        super(description, TaskType.DEADLINE);
        this.date = null;
        this.dateTime = Objects.requireNonNull(dateTime);
    }

    //factory to build from a raw /by... string
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
        String when = (dateTime != null) ? dateTime.format(PRINT_DT) : date.format(PRINT_DATE);
        return super.toString() + " (by: " + when + ")";
    }
}