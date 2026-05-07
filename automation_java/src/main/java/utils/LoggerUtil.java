package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for logging operations.
 * Provides centralized logging configuration.
 */
public class LoggerUtil {
    private final Logger logger;

    public LoggerUtil() {
        this.logger = LogManager.getLogger(this.getClass());
    }

    public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void warn(String message) {
        logger.warn(message);
    }
}
