package lilbird.command;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * AI-ASSISTED NOTE
 * Tool: ChatGPT (assistant) on 2025-09-16
 * Scope: Test outline for `AddDeadlineCommand.fromArgs(...)` parsing "<desc> /by <time>".
 * What it generated: Positive cases (/by present, non-empty parts) and negative cases (missing /by, empty desc/time).
 * Verification: `./gradlew test`; then manual.
 */
class AddDeadlineCommandFactoryTest {
    @Test
    void fromArgs_valid_buildsCommand() throws Exception {
        assertNotNull(AddDeadlineCommand.fromArgs("submit report /by 2025-12-01"));
        assertNotNull(AddDeadlineCommand.fromArgs("pay bills /by 2025-12-01 1830"));
    }

    @Test
    void fromArgs_missingBy_throws() {
        assertThrows(LilBirdException.class, () -> AddDeadlineCommand.fromArgs("missing by"));
    }

    @Test
    void fromArgs_emptyParts_throws() {
        assertThrows(LilBirdException.class, () -> AddDeadlineCommand.fromArgs("/by 2025-12-01"));  // empty desc
        assertThrows(LilBirdException.class, () -> AddDeadlineCommand.fromArgs("desc /by   "));    // empty time
    }
}
