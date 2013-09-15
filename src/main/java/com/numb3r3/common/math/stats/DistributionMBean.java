package com.numb3r3.common.math.stats;

/**
 * Abstract MBean interface for objects that describe the general
 * characteristics of a distribution of (double) values.
 * This interface supports the standard MBean management interface,
 * so implementing classes will support JMX monitoring.
 *
 * @author netflixoss $
 * @version $Revision: $
 */
public interface DistributionMBean {

    /**
     * Clears out the distribution, resetting it to its initial state.
     */
    void clear();

    /**
     * Get the number of values in the distribution.
     */
    long getNumValues();

    /**
     * Get the average value in the distribtion.
     */
    double getMean();

    /**
     * Get the variance (the square of the standard deviation)
     * of values in the distribution.
     */
    double getVariance();

    /**
     * Get the standard deviation of values in the distribution.
     */
    double getStdDev();

    /**
     * Get the minimum value found in the distribution.
     */
    double getMinimum();

    /**
     * Get the maximum value found in the distribution.
     */
    double getMaximum();

} // DistributionMBean