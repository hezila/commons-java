package com.numb3r3.common;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.numb3r3.common.collection.ArrayUtil;
import com.numb3r3.common.math.Matrix;
import com.numb3r3.common.math.local.InMemoryJBlasMatrix;
import com.numb3r3.common.math.stat.Statistics;
import com.numb3r3.common.util.ArrayPrinting;
import org.apache.commons.math.special.Gamma;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class MathUtil {

	/** The constant 1 / sqrt(2 pi) */
	public static final double PI = 0.3989422804014327028632;

	/** The constant - log( sqrt(2 pi) ) */
	public static final double logPI = -0.9189385332046726695410;

	/**
	 * sqrt(a^2 + b^2) without under/overflow.
	 */
	public static double hypot(double a, double b) {
		double r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		} else {
			r = 0.0;
		}
		return r;
	}



	/* methods for normal distribution */

	/**
	 * Returns the cumulative probability of the standard normal.
	 * 
	 * @param x
	 *            the quantile
	 */
	public static double pnorm(double x) {
		return Statistics.normalProbability(x);
	}

	/**
	 * Returns the cumulative probability of a normal distribution.
	 * 
	 * @param x
	 *            the quantile
	 * @param mean
	 *            the mean of the normal distribution
	 * @param sd
	 *            the standard deviation of the normal distribution.
	 */
	public static double pnorm(double x, double mean, double sd) {
		if (sd <= 0.0)
			throw new IllegalArgumentException("standard deviation <= 0.0");
		return pnorm((x - mean) / sd);
	}

	public static double mean(double[] arrays) {
        assert arrays.length > 0;
		double value = 0.0;
		for (double v : arrays) {
			value += v;
		}
		value = value/arrays.length;
		return value;
	}

	public static double std(double[] arrays) {
		double mean = mean(arrays);
		double err = 0.0;
		for (double v : arrays) {
			err += (v - mean) * (v - mean);
		}
		err = err/arrays.length;
		return Math.sqrt(err);
	}

	/*
	 * given log(a) and log(b), return log(a + b)
	 */
	public static double logSum(double log_a, double log_b) {
		double v;

		if (log_a < log_b) {
			v = log_b + Math.log(1 + Math.exp(log_a - log_b));
		} else {
			v = log_a + Math.log(1 + Math.exp(log_b - log_a));
		}
		return (v);
	}

	static boolean GAMMAACCURATE = true;

	static final double HALFLN2PI = 9.189385332046727E-001;

	static final double HALFLN2 = 3.465735902799726e-001;

	static final double INV810 = 1.234567901234568E-003;

	public static double lgamma(double x) {
		double lnx, einvx;
		double prec;
		assert x > 0;
		lnx = Math.log(x);
		einvx = Math.exp(1. / x);

		if (GAMMAACCURATE) {
			prec = x * x * x;
			prec *= prec;
			prec = INV810 / prec;
			/*
			 * y = x * ( log(x) - 1 + .5 * log(x * sinh(1/x) + prec) ) - .5 *
			 * log(x) + .5 * log(2 * pi)
			 */
			return x
					* (lnx - 1. + .5 * Math.log(x * (einvx - 1. / einvx) / 2.
							+ prec)) - .5 * lnx + HALFLN2PI;
		} else {
			/*
			 * y = x * ( 1.5 * log(x) - 1 + .5 * log(exp(1/x) - 1/exp(1/x)) - .5
			 * * log(2) ) - .5 * log(x) + .5 * log(2 * pi);
			 */
			return x
					* (1.5 * lnx - 1. + .5 * Math.log(einvx - 1. / einvx) - HALFLN2)
					- .5 * lnx + HALFLN2PI;
		}
	}

	public static double log_gamma(double x) {
		double z;
		assert x > 0;
		z = 1. / (x * x);

		x = x + 6;
		z = (((-0.000595238095238 * z + 0.000793650793651) * z - 0.002777777777778)
				* z + 0.083333333333333)
				/ x;
		z = (x - 0.5) * Math.log(x) - x + 0.918938533204673 + z
				- Math.log(x - 1) - Math.log(x - 2) - Math.log(x - 3)
				- Math.log(x - 4) - Math.log(x - 5) - Math.log(x - 6);
		return z;
	}

	public static double gamma(double x) {

		return Math.exp(log_gamma(x));
	}

	/*
	 * taylor approximation of first derivative of the log gamma function
	 * (Abramowitz and Stegun, 1970)
	 */

	public static double digamma(double x) {
		double p;
		assert x > 0;
		x = x + 6;
		p = 1 / (x * x);
		p = (((0.004166666666667 * p - 0.003968253986254) * p + 0.008333333333333)
				* p - 0.083333333333333)
				* p;
		p = p + Math.log(x) - 0.5 / x - 1 / (x - 1) - 1 / (x - 2) - 1 / (x - 3)
				- 1 / (x - 4) - 1 / (x - 5) - 1 / (x - 6);
		return p;
	}

	public static double trigamma(double x) {
		return Gamma.trigamma(x);
	}

	static NumberFormat nf = new DecimalFormat("0.00000");

	/**
	 * @param d
	 * @return
	 */
	public static String formatDouble(double d) {
		String x = nf.format(d);
		// String x = shadeDouble(d, 1);
		return x;

	}

	static String[] shades = { "     ", ".    ", ":    ", ":.   ", "::   ",
			"::.  ", ":::  ", ":::. ", ":::: ", "::::.", ":::::" };

	static NumberFormat lnf = new DecimalFormat("00E0");

	/**
	 * create a string representation whose gray value appears as an indicator
	 * of magnitude, cf. Hinton diagrams in statistics.
	 * 
	 * @param d
	 *            value
	 * @param max
	 *            maximum value
	 * @return
	 */
	public static String shadeDouble(double d, double max) {
		int a = (int) Math.floor(d * 10 / max + 0.5);
		if (a > 10 || a < 0) {
			String x = lnf.format(d);
			a = 5 - x.length();
			for (int i = 0; i < a; i++) {
				x += " ";
			}
			return "<" + x + ">";
		}
		return "[" + shades[a] + "]";
	}

	public static int[] range(int n) {
		int[] ranges = new int[n];
		for (int i = 0; i < n; i++) {
			ranges[i] = i;
		}
		return ranges;
	}

	public static int argmax(int[] arrays) {
		int arg = 0;
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i] > arrays[arg]) {
				arg = i;
			}
		}
		return arg;
	}

	public static int argmin(int[] arrays) {
		int arg = 0;
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i] < arrays[arg]) {
				arg = i;
			}
		}

		return arg;
	}

	public static int argmax(double[] arrays) {
		int arg = 0;
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i] > arrays[arg]) {
				arg = i;
			}
		}
		return arg;
	}

	public static int argmin(double[] arrays) {

		int arg = 0;
		for (int i = 0; i < arrays.length; i++) {

			if (arrays[i] < arrays[arg]) {
				arg = i;
			}
		}

		return arg;
	}




	public static List<Pair> sortcopy(List<Pair> pairs) {
		return Ordering.natural().reverse().sortedCopy(pairs);
	}

	public static List<Pair> greatestOf(List<Pair> pairs, int top) {
		return Ordering.natural().greatestOf(pairs, top);
	}

	public static void hist(List<Integer> members) {
		Map<Integer, Double> hist = Maps.newHashMap();
		int size = members.size();
		for (int i = 0; i < members.size(); i++) {
			int m = members.get(i);
			if (hist.containsKey(m)) {
				hist.put(m, hist.get(m) + 1);
			} else {
				hist.put(m, 1.0);
			}
		}
		System.out.print("{");
		for (int key : hist.keySet()) {
			hist.put(key, hist.get(key) / (size + 0.0));
			System.out.print("[K: " + key + " V: " + hist.get(key) + "], ");
		}
		System.out.println("}");

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Matrix m = InMemoryJBlasMatrix.randn(3);
		double[] scores1 = m.toArray();
		double[] scores2 = m.mult(-1.0).toArray();

		ArrayPrinting.printDoubleArray(scores1, null, "score 1");
        ArrayPrinting.printDoubleArray(scores2, null, "score 2");
        ArrayPrinting.printIntArray(ArrayUtil.argsort(scores2), null, "sorted array");

		System.out.println("Max:" + MathUtil.argmax(scores1));
		System.out.println("Max: " + MathUtil.argmax(scores2));
		System.out.println("STD:" + MathUtil.std(scores1));
	}

}
