package com.numb3r3.common.math.stat;

import com.numb3r3.common.math.Matrix;
import com.numb3r3.common.math.local.InMemoryJBlasMatrix;

/**
 * Created with IntelliJ IDEA. User: numb3r3 Date: 13-3-18 Time: 上午10:45
 * Function to compute the Gaussian pdf (probability density function) and the
 * Gaussian cdf (cumulative density function) from:
 * http://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
 */
public class Gaussian {
	/** The constant 1 / sqrt(2 pi) */
	public static final double PSI = 0.3989422804014327028632;

	/** The constant - log( sqrt(2 pi) ) */
	public static final double logPSI = -0.9189385332046726695410;

	/** Distribution type: undefined */
	public static final int undefinedDistribution = 0;

	/** Distribution type: noraml */
	public static final int normalDistribution = 1;

	/** Distribution type: chi-squared */
	public static final int chisqDistribution = 2;

	private double mean = 0.0;
	private double sigma = 1.0;
	private Matrix mu = null;
	private Matrix cox = null;

	public Gaussian(double mean, double sigma) {
		this.mean = mean;
		this.sigma = sigma;
	}

	public Gaussian(Matrix mu, Matrix cox) {
		this.mu = mu;
		this.cox = cox;
	}

	public double sigma() {
		return sigma;
	}

	public double mean() {
		return this.mean;
	}

	/**
	 * Returns the density of the standard normal.
	 * 
	 * @param x
	 *            the quantile
	 * @return the density
	 */
	public static double dnorm(double x) {
		return Math.exp(-x * x / 2.) * PSI;
	}

	/**
	 * Returns the density value of a standard normal.
	 * 
	 * @param x
	 *            the quantile
	 * @param mean
	 *            the mean of the normal distribution
	 * @param sd
	 *            the standard deviation of the normal distribution.
	 * @return the density
	 */
	public static double dnorm(double x, double mean, double sd) {
		if (sd <= 0.0)
			throw new IllegalArgumentException("standard deviation <= 0.0");
		return dnorm((x - mean) / sd);
	}

	/**
	 * Returns the log-density of the standard normal.
	 * 
	 * @param x
	 *            the quantile
	 * @return the density
	 */
	public static double dnormLog(double x) {
		return logPSI - x * x / 2.;
	}

	/**
	 * Returns the log-density value of a standard normal.
	 * 
	 * @param x
	 *            the quantile
	 * @param mean
	 *            the mean of the normal distribution
	 * @param sd
	 *            the standard deviation of the normal distribution.
	 * @return the density
	 */
	public static double dnormLog(double x, double mean, double sd) {
		if (sd <= 0.0)
			throw new IllegalArgumentException("standard deviation <= 0.0");
		return -Math.log(sd) + dnormLog((x - mean) / sd);
	}

	/* return phi(x) = standard Gaussian pdf */
	public static double phi(double x) {
		return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
	}

	public static double mphi(Matrix value, Matrix mu, Matrix cox) {
		int k = value.getLength();
		double det = cox.det();

		double part1 = Math.exp(-0.5 * k * Math.log(2 * Math.PI));

		double part2 = Math.pow(det, -0.5);

		Matrix dev = value.sub(mu);

		double part3 = Math.exp(-0.5
				* (dev.transpose().product(cox.inverse())).dot(dev));
		return part1 * part2 * part3;

	}

	public static double mphi(Matrix value, Matrix mu, Matrix cox, Matrix inv) {
		int k = value.getLength();
		double det = cox.det();

		double part1 = Math.exp(-0.5 * k * Math.log(2 * Math.PI));

		double part2 = Math.pow(det, -0.5);

		Matrix dev = value.sub(mu);

		double part3 = Math.exp(-0.5 * (dev.transpose().product(inv)).dot(dev));
		return part1 * part2 * part3;

	}

	/* return phi(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma */
	public static double phi(double x, double mu, double sigma) {
		return phi((x - mu) / sigma) / sigma;
	}

	/* return Phi(z) = standard Gaussian cdf using Taylor approximation */
	public static double Phi(double z) {
		if (z < -8.0)
			return 0.0;
		if (z > 8.0)
			return 1.0;
		double sum = 0.0, term = z;
		for (int i = 3; sum + term != sum; i += 2) {
			sum = sum + term;
			term = term * z * z / i;
		}
		return 0.5 + sum * phi(z);
	}

	/* return Phi(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma */
	public static double Phi(double z, double mu, double sigma) {
		return Phi((z - mu) / sigma);
	}

	/* Compute z such that Phi(z) = y via bisection search */
	public static double PhiInverse(double y) {
		return PhiInverse(y, .00000001, -8, 8);
	}

	/* bisection search */
	private static double PhiInverse(double y, double delta, double lo,
			double hi) {
		double mid = lo + (hi - lo) / 2;
		if (hi - lo < delta)
			return mid;
		if (Phi(mid) > y)
			return PhiInverse(y, delta, lo, mid);
		else
			return PhiInverse(y, delta, mid, hi);
	}

	public static Matrix oneCox(int k) {
		Matrix cox = InMemoryJBlasMatrix.eye(k);

		return null;
	}

	public static Matrix sample(Matrix mu, Matrix Sigma) {
		Matrix values = InMemoryJBlasMatrix.randn(mu.getRowsNum());
		return mu.add(Sigma.product(values));
	}

	public static double sample(double mean, double stddev) {
		return RandomUtil.gaussian(mean, stddev);
	}

	public static double hellinger_distance(Gaussian g1, Gaussian g2) {
		double distance = 1.0;
		double sigma1 = g1.sigma();
		double sigma2 = g2.sigma();
		double mean1 = g1.mean();
		double mean2 = g2.mean();
		double diff = mean1 - mean2;
		distance -= Math.sqrt((2.0 * sigma1 * sigma2)
				/ (sigma1 * sigma1 + sigma2 * sigma2))
				* Math.exp(-0.25
						* ((diff * diff) / (sigma1 * sigma1 + sigma2 * sigma2)));
		return distance;
	}

	public static void main(String[] args) {
		// 2.1, 3.5, 8. , 9.5
		Matrix x = InMemoryJBlasMatrix.randn(4);
		x.put(0, 0, 2.1);
		x.put(1, 0, 3.5);
		x.put(2, 0, 8.0);
		x.put(3, 0, 9.5);

		Matrix mu = InMemoryJBlasMatrix.zeros(4);
		mu.put(0, 0, 2.0);
		mu.put(1, 0, 3.0);
		mu.put(2, 0, 8.0);
		mu.put(3, 0, 10.0);

		Matrix Sigma = InMemoryJBlasMatrix.eye(4);
		Sigma.put(0, 0, 2.3);
		Sigma.put(1, 1, 1.5);
		Sigma.put(2, 2, 1.7);
		Sigma.put(3, 3, 2.0);

		System.out.println("Det: " + Sigma.det());
		System.out.println("Normal density: " + Gaussian.mphi(x, mu, Sigma));
		// System.out.println("PDF: " + Gaussian.pdf());

		double y = 0.1;
		double mean = 1.5;
		double sig = 2.0;
		System.out.println("NORM: " + Gaussian.phi(y, mean, sig));
	}

}
