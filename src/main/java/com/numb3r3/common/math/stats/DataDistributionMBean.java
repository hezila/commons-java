package com.numb3r3.common.math.stats;

/**
 * Abstract MBean interface for objects that hold information about a distribution
 * of (double) values.  The information includes basic statistics (count, mean,
 * min, max) as well as information about the percentile values for some number
 * of percent values.
 * <p/>
 * This interface supports the standard MBean management interface,
 * so implementing classes will support JMX monitoring.
 *
 * @author netflixoss $
 * @version $Revision: $
 */
public interface DataDistributionMBean extends DistributionMBean {

    /**
     * Gets a String representation of the time when this data was produced.
     */
    String getTimestamp();

    /**
     * Gets the time when this data was produced, in milliseconds since the epoch.
     */
    long getTimestampMillis();

    /**
     * Gets the length of time over which the data was collected,
     * in milliseconds.
     */
    long getSampleIntervalMillis();

    /**
     * Gets the number of values used to compute the percentile values.
     * This value may be smaller than the value of {@link #getNumValues}
     * depending on how the percentile values were computed.
     */
    int getSampleSize();

    /**
     * Gets the array of known percentile percents.
     */
    double[] getPercents();

    /**
     * Gets the array of known percentile values.
     */
    double[] getPercentiles();

} // DataDistributionMBean