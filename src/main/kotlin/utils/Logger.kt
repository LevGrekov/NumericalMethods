package utils

import org.slf4j.LoggerFactory

object Logger {
    private val logger = LoggerFactory.getLogger(Logger::class.java)
    fun myFunction() {
        logger.info("This is an informational message")
        logger.error("This is an error message")
    }
}