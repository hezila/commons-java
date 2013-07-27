package com.numb3r3.common;

/**
 * An interface for processing errors and e.g. writing to
 * a log or console.
 */
public interface ErrorProcessor {

    /**
     * Write an error message.
     *
     * @param message the error message
     */
    void error(String message);

    /**
     * Write a warning message.
     *
     * @param message the warning message
     */
    void warning(String message);
}
