// src/test/java/lilbird/task/DeadlineTest.java
package lilbird.task;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {

    @Test
    void fromUserInput_dateOnly_parses() throws LilBirdException {
        Task t = Deadline.fromUserInput("Submit iP", "2025-09-01");
        String s = t.toString();
        assertTrue(s.contains("Submit iP"));
        assertTrue(s.contains("by")); // relies on your toString format
    }

    @Test
    void fromUserInput_dateTime_parses() throws LilBirdException {
        Task t = Deadline.fromUserInput("Project", "2025-09-01 1800");
        String s = t.toString();
        assertTrue(s.contains("Project"));
        assertTrue(s.toLowerCase().contains("pm") || s.matches(".*18:00.*"));
    }

    @Test
    void fromUserInput_badFormat_throws() {
        assertThrows(LilBirdException.class,
                () -> Deadline.fromUserInput("X", "01-09-2025")); // wrong format
    }
}

