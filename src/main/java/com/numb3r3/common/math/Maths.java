package com.numb3r3.common.math;

import com.numb3r3.common.metrics.PearsonCorrelation;

import java.util.Random;

public class Maths {

    public static final boolean closeToOne(double number) {
        return Math.abs(number - 1) < 1.E-10;
    }

    public static final boolean closeToZero(double number) {
        return Math.abs(number) < 1.E-5;
    }

    public static boolean almost(double a, double b, double prec) {
        return Math.abs(a - b) / Math.abs(a + b) <= prec || (almostZero(a) && almostZero(b));
    }

    public static boolean almost(double a, double b) {
        return Math.abs(a - b) / Math.abs(a + b) <= 1e-10 || (almostZero(a) && almostZero(b));
    }

    public static boolean almostZero(double a) {
        return Math.abs(a) <= 1e-30;
    }


    /**
     * Returns the square of a value
     *
     * @param x
     * @return the square
     */
    public static double square(double x) {
        return x * x;
    }

    public static int sum(int[] array) {
        int res = 0;
        for (int i = 0; i < array.length; i++) {
            res += array[i];
        }

        return res;
    }

    public static double sum(double[] ds) {
        double res = 0;
        for (int i = 0; i < ds.length; i++) {
            res += ds[i];
        }
        return res;
    }

    public static double max(double[] ds) {
        double res = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < ds.length; i++) {
            res = Math.max(res, ds[i]);
        }
        return res;
    }

    public static double min(double[] ds) {
        double res = Double.POSITIVE_INFINITY;
        for (int i = 0; i < ds.length; i++) {
            res = Math.min(res, ds[i]);
        }
        return res;
    }

    /**
     * Return a ramdom multinominal distribution.
     *
     * @param size
     * @return
     */
    public static final double[] randomVector(int size, Random r) {
        double[] random = new double[size];
        double sum = 0;
        for (int i = 0; i < size; i++) {
            double number = r.nextDouble();
            random[i] = number;
            sum += number;
        }
        for (int i = 0; i < size; i++) {
            random[i] = random[i] / sum;
        }
        return random;
    }


    public static double L2Norm(double[] a) {
        double result = 0.0;
        for (double v : a)
            result += square(v);
        return Math.sqrt(result);
    }

    public static double KLDistance(double[] p, double[] q) {
        int len = p.length;
        double kl = 0;
        for (int j = 0; j < len; j++) {
            if (p[j] == 0 || q[j] == 0) {
                continue;
            } else {
                kl += q[j] * Math.log(q[j] / p[j]);
            }

        }
        return kl;
    }


    /**
     * The squared norm of a vector
     *
     * @param v
     * @return
     */
    public static double twoNormSquared(double[] v) {
        double result = 0.0;
        for (double d : v)
            result += d * d;
        return result;
    }

    public static double frobeniusNorm(double[] scores) {
        double value = 0.0;
        for (int i = 0; i < scores.length; i++) {
            value += scores[i] * scores[i];
        }
        return Math.sqrt(value);
    }


    public static double L2Distance(double[] p, double[] q) {
        int len = p.length;
        double l2 = 0;
        for (int j = 0; j < len; j++) {
            if (p[j] == 0 || q[j] == 0) {
                continue;
            } else {
                l2 += (q[j] - p[j]) * (q[j] - p[j]);
            }

        }
        return Math.sqrt(l2);
    }


    public static double L1Distance(double[] p, double[] q) {
        int len = p.length;
        double l1 = 0;
        for (int j = 0; j < len; j++) {
            if (p[j] == 0 || q[j] == 0) {
                continue;
            } else {
                l1 += Math.abs(q[j] - p[j]);
            }

        }
        return l1;
    }


    public static double cosine(double[] a,
                                double[] b) {
        return (dot(a, b) + 1e-5) / (Math.sqrt(dot(a, a) + 1e-5) * Math.sqrt(dot(b, b) + 1e-5));
    }

    public static double pearson_correlation(double[] a, double[] b) {
        return PearsonCorrelation.correlation(a, b);
    }


    public static double dot(double[] ds, double[] ds2) {
        double res = 0;
        for (int i = 0; i < ds2.length; i++) {
            res += ds[i] * ds2[i];
        }
        return res;
    }

    public static double expDigamma(double number) {
        return Math.exp(digamma(number));
    }

    public static double digamma(double number) {
        if (number > 7) {
            return digammApprox(number - 0.5);
        } else {
            return digamma(number + 1) - 1.0 / number;
        }
    }

    private static double digammApprox(double value) {
        return Math.log(value) + 0.04167 * Math.pow(value, -2) - 0.00729 * Math.pow(value, -4)
                + 0.00384 * Math.pow(value, -6) - 0.00413 * Math.pow(value, -8);
    }

    public static double eulerGamma = 0.57721566490152386060651209008240243;

    // FIXME -- so far just the initialization from Minka's paper "Estimating a Dirichlet distribution".
    public static double invDigamma(double y) {
        if (y >= -2.22) return Math.exp(y) + 0.5;
        return -1.0 / (y + eulerGamma);
    }


    public static boolean equalWithinFPError(double a, double b) {
        if (a == b) {
            return true;
        }

        double nearlyZero = a - b;
        // these errors are generally in the vicinity of 1e-15
        // let's be extra permissive, 1e-10 is good enough anyway
        return -1e-10 < nearlyZero && nearlyZero < 1e-10;
    }

    public static void longIntoByteArray(long l, byte[] array, int offset) {
        int i, shift;
        for(i = 0, shift = 56; i < 8; i++, shift -= 8)
            array[offset+i] = (byte)(0xFF & (l >> shift));
    }

    public static byte[] long2ByteArray(long l) {
        byte[] array = new byte[8];
        int i, shift;
        for(i = 0, shift = 56; i < 8; i++, shift -= 8) {
            array[i] = (byte)(0xFF & (l >> shift));
        }
        return array;
    }

    public static long byteArrayIntoLong(byte [] bytearray) {
        return byteArrayIntoLong(bytearray, 0);
    }

    public static long byteArrayIntoLong(byte [] bytearray,
                                         int offset) {
        long result = 0;
        for (int i = offset; i < 8 /*Bytes in long*/; i++) {
            result = (result << 8 /*Bits in byte*/) |
                    (0xff & (byte)(bytearray[i] & 0xff));
        }
        return result;
    }

    public static byte[] int2ByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static int byteArray2Int(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static long byteArray2Long(byte[] b) {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
