package lilbird.command;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * AI-ASSISTED NOTE
 * Tool: ChatGPT (assistant) on 2025-09-16
 * Scope: Sketched the structure of factory-method tests for `AddTodoCommand.fromArgs(...)`.
 * What it generated: Happy-path test (non-empty args) and error-path tests (blank/null).
 * Rationale: Focus tests on the factory’s responsibilities; avoid hitting execute()/storage/UI here.
 * Verification: Ran `./gradlew test` and manually tried `todo` commands; ensured failures point at incorrect arg handling.
 */

class AddTodoCommandFactoryTest {
    @Test
    void fromArgs_valid_buildsCommand() throws Exception {
        assertNotNull(AddTodoCommand.fromArgs("read book"));
        assertNotNull(AddTodoCommand.fromArgs("  trim me   "));
    }

    @Test
    void fromArgs_blank_throws() {
        assertThrows(LilBirdException.class, () -> AddTodoCommand.fromArgs("   "));
        assertThrows(LilBirdException.class, () -> AddTodoCommand.fromArgs(null));
    }
}
