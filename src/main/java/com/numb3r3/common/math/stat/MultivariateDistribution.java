package com.numb3r3.common.math.stat;

import com.numb3r3.common.math.Matrix;

/**
 * from
 * http://code.google.com/p/java-statistical-analysis-tool/source/browse/trunk
 * /JSAT/src/jsat/distributions/multivariate/MultivariateDistribution.java?r=231
 * 
 * @author numb3r3
 * 
 */
public interface MultivariateDistribution{
	/**
	 * Computes the log of the probability density function. If the probability
	 * of the input is zero, the log of zero would be
	 * {@link Double#NEGATIVE_INFINITY}. Instead, -{@link Double#MAX_VALUE} is
	 * returned.
	 * 
	 * @param x
	 *            the array for the vector the get the log probability of
	 * @return the log of the probability.
	 * @throws ArithmeticException
	 *             if the vector is not the correct length, or the distribution
	 *             has not yet been set
	 */
	public double logPdf(double[] x);

	/**
	 * Computes the log of the probability density function. If the probability
	 * of the input is zero, the log of zero would be
	 * {@link Double#NEGATIVE_INFINITY}. Instead, -{@link Double#MAX_VALUE} is
	 * returned.
	 * 
	 * @param x
	 *            the matrix the get the log probability of
	 * @return the log of the probability.
	 * @throws ArithmeticException
	 *             if the vector is not the correct length, or the distribution
	 *             has not yet been set
	 */
	public double logPdf(Matrix x);

	/**
	 * Returns the probability of a given vector from this distribution. By
	 * definition, the probability will always be in the range [0, 1].
	 * 
	 * @param x
	 *            the array of the vector the get the log probability of
	 * @return the probability
	 * @throws ArithmeticException
	 *             if the vector is not the correct length, or the distribution
	 *             has not yet been set
	 */
	public double pdf(double[] x);

	/**
	 * Returns the probability of a given vector from this distribution. By
	 * definition, the probability will always be in the range [0, 1].
	 * 
	 * @param x
	 *            the matrix the get the log probability of
	 * @return the probability
	 * @throws ArithmeticException
	 *             if the vector is not the correct length, or the distribution
	 *             has not yet been set
	 */
	public double pdf(Matrix x);

	/**
	 * Sets the parameters of the distribution to attempt to fit the given
	 * matrix.
	 * 
	 * @param dataSet
	 *            the list of data points
	 * @return <tt>true</tt> if the distribution was fit to the data, or
	 *         <tt>false</tt> if the distribution could not be fit to the data
	 *         set.
	 */
	public boolean setUsingData(Matrix dataSet);

}
