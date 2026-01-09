package it.unibo.samplejavafx;

import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test to verify logging functionality with Logback.
 */
@Slf4j
class LoggingTest {

    @Test
    void testLoggingIsConfigured() {
        log.info("Testing logging system...");
        log.debug("Debug message");
        log.warn("Warning message");
        log.error("Error message");
        
        assertNotNull(log);
    }
}
