package lilbird.command;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * AI-ASSISTED NOTE
 * Tool: ChatGPT (assistant) on 2025-09-16
 * Scope: Tests for `AddEventCommand.fromArgs(...)` parsing "<desc> /from <start> /to <end>".
 * What it generated: Valid case; missing markers; wrong order; empty segments.
 * My adaptations: Kept the factory as the unit under test (no I/O), and matched our current error wording.
 * Verification: `./gradlew test`; manual: several `event ... /from ... /to ...` variations in the app.
 */

class AddEventCommandFactoryTest {
    @Test
    void fromArgs_valid_buildsCommand() throws Exception {
        String ok = "CS2103T lecture /from 2025-09-20 1000 /to 2025-09-20 1200";
        assertNotNull(AddEventCommand.fromArgs(ok));
    }

    @Test
    void fromArgs_missingMarkers_throws() {
        assertThrows(LilBirdException.class, () -> AddEventCommand.fromArgs("desc only"));
        assertThrows(LilBirdException.class, () -> AddEventCommand.fromArgs("d /from x"));
        assertThrows(LilBirdException.class, () -> AddEventCommand.fromArgs("d /to y"));
    }

    @Test
    void fromArgs_wrongOrder_throws() {
        String wrong = "hack /to 2025-11-03 2100 /from 2025-11-03 0900";
        assertThrows(LilBirdException.class, () -> AddEventCommand.fromArgs(wrong));
    }

    @Test
    void fromArgs_emptySegments_throws() {
        assertThrows(LilBirdException.class, () ->
                AddEventCommand.fromArgs("talk /from 2025-11-03 0900 /to "));
        assertThrows(LilBirdException.class, () ->
                AddEventCommand.fromArgs("  /from 2025-11-03 0900 /to 2025-11-03 1000"));
    }
}
