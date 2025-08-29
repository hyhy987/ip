package lilbird.parser;

import lilbird.exception.LilBirdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    void parseIndex_valid_middleOfRange_returnsIndex() throws LilBirdException {
        int idx1 = Parser.parseIndex1Based("  3  ", 5);
        assertEquals(3, idx1);
    }

    @Test
    void parseIndex_nonNumeric_throws() {
        LilBirdException ex = assertThrows(
                LilBirdException.class,
                () -> Parser.parseIndex1Based("abc", 5)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("valid"));
    }

    @Test
    void parseIndex_outOfRange_throws() {
        LilBirdException ex = assertThrows(
                LilBirdException.class,
                () -> Parser.parseIndex1Based("6", 5)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("doesn"));
    }
}

