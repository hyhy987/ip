package lilbird.task;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeadlineTest {

    @Test
    void fromUserInput_dateOnly_serializesIsoDate() throws LilBirdException {
        Task t = Deadline.fromUserInput("Submit iP", "2025-09-01");
        String ser = t.serialize();
        // Format: D | 0/1 | description | yyyy-MM-dd  (no tags suffix when empty)
        assertTrue(ser.startsWith("D | 0 | "), "should be a Deadline and not done");
        assertTrue(ser.contains("Submit iP"), "description should be present");
        assertTrue(ser.endsWith("2025-09-01"), "date-only should serialize as ISO yyyy-MM-dd");
    }

    @Test
    void fromUserInput_rejectsWrongFormats() {
        // Wrong delimiter order/format
        assertThrows(LilBirdException.class,
                () -> Deadline.fromUserInput("X", "01-09-2025"));

        // Uses colon in time => "yyyy-MM-dd HHmm" expected; this should fail
        assertThrows(LilBirdException.class,
                () -> Deadline.fromUserInput("Y", "2025-09-01 18:00"));

        // Extra junk, wrong length
        assertThrows(LilBirdException.class,
                () -> Deadline.fromUserInput("Z", "2025/09/01"));
    }

}
