package com.numb3r3.common;

/**
 * An {@link ErrorProcessor} that writes to {@link System.err}.
 */
public final class SystemErrorProcessor implements ErrorProcessor {

    /* (non-Javadoc)
     * @see org.reclab.core.util.ErrorProcessor#error(java.lang.String)
     */
    @Override
    public void error(final String message) {
        System.err.println(message);
    }

    /* (non-Javadoc)
     * @see org.reclab.core.util.ErrorProcessor#warning(java.lang.String)
     */
    @Override
    public void warning(final String message) {
        System.err.println(message);
    }

}
