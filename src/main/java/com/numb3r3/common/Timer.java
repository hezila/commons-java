package com.numb3r3.common;

/**
 * A timer that can be used to determine the elapsed time
 * of an operation or series of operations. Normal use is
 * to set up a timer, {@link #start()} it, run the code you
 * want to time, {@link #stop()} the timer and then see how
 * long it took to run with {@link #getElapsedMillis()} or {@link #toString()}.
 * It is also possible to add up the cumulative time of a series
 * of operations with a series of calls to {@link #start()} and {@link #stop()}
 * as in the following example:
 * <pre>
 * long doTime() {
 *     Timer timer = new Timer();
 *
 *     // Time doA().
 *     timer.start();
 *     doA();
 *     timer.stop();
 *
 *     // Don't time this call.
 *     dontTimeMe();
 *
 *     // Time doB().
 *     timer.start();
 *     doB();
 *     timer.stop();
 *
 *     return timer.getElapsedMillis();
 * }
 * </pre>
 * The value returned will be the time spent in <code>doA()</code> and <code>doB()</code>
 * but will not count the time spent in <code>dontTimeMe()</code> because the
 * timer was stopped while it ran.
 */
public final class Timer {

    /**
     * Milliseconds per second.
     */
    private static final int MILLIS_PER_SECOND = 1000;

    /**
     * Is the timer currently running?
     */
    private boolean running = false;

    /**
     * How many total milliseconds have elapsed while the
     * timer was is running mode since it was constructed
     * or the last time it was reset.
     */
    private long elapsedMillis = 0L;

    /**
     * The time at which the timer was last started.
     */
    private long lastStartMillis;

    /**
     * Start the timer. Elapsed milliseconds as queried by
     * {@link #getElapsedMillis()} will start to advance.
     * If the timer is already running, then this has no
     * effect. It continues to run as if the call had not been
     * made.
     */
    public void start() {
        if (!running) {
            lastStartMillis = System.currentTimeMillis();
            running = true;
        }
    }

    /**
     * Stop the timer. Elapsed milliseconds as queried by
     * {@link #getElapsedMillis()} will no longer advance.
     * If the timer is already stopped, then this has no
     * effect. It is as if the call had not been
     * made.
     */
    public void stop() {
        if (running) {
            long stopMillis = System.currentTimeMillis();
            running = false;
            elapsedMillis += (stopMillis - lastStartMillis);
        }
    }

    /**
     * Get the total number of milliseconds that have
     * elapsed while the timer was in running mode since
     * it was constructed or last reset via {@link Timer#reset()}.
     *
     * @return the number of milliseconds spend running.
     */
    public long getElapsedMillis() {
        if (running) {
            long currentMillis = System.currentTimeMillis();
            return elapsedMillis + (currentMillis - lastStartMillis);
        } else {
            return elapsedMillis;
        }
    }

    /**
     * This implementation nicely formats the elapsed time
     * returned by {@link #getElapsedMillis()}.
     *
     * @return the formatted time, in milliseconds if under a second
     *         and seconds otherwise.
     */
    @Override
    public String toString() {

        long elapsed = getElapsedMillis();

        if (elapsed < MILLIS_PER_SECOND) {
            return String.format("%dms", elapsed);
        } else {
            return String.format("%d.%03ds", elapsed / MILLIS_PER_SECOND, elapsed % MILLIS_PER_SECOND);
        }
    }

    /**
     * Reset the time so that an immediate subsequent call to
     * {@link #getElapsedMillis()} will return 0. This can be
     * done whether the timer is running or not.
     */
    public void reset() {

        elapsedMillis = 0L;

        if (running) {
            lastStartMillis = System.currentTimeMillis();
        }
    }
}
