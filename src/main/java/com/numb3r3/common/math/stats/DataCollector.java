package com.numb3r3.common.math.stats;

/**
 * An object that collects new values incrementally.
 *
 * @author netflixoss
 * @version $Revision: $
 */
public interface DataCollector {

    /**
     * Adds a value to the collected data.
     * This must run very quickly, and so can safely
     * be called in time-critical code.
     */
    void noteValue(double val);

} // DataCollector