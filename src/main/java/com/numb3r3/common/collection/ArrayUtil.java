package com.numb3r3.common.collection;

import com.numb3r3.common.math.Maths;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class ArrayUtil {

    public static int[] argsort(final double[] a) {
        return argsort(a, true);
    }

    public static int[] argsort(final double[] a, final boolean ascending) {
        Integer[] indexes = new Integer[a.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (ascending ? 1 : -1) * Double.compare(a[i1], a[i2]);
            }
        });
        return asArray(indexes);
    }

    public static <T extends Number> int[] asArray(final T... a) {
        int[] b = new int[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[i].intValue();
        }
        return b;
    }

    public static int[] createIntArray(int length, int value) {
        int[] values = new int[length];
        for (int i = 0; i < length; i++) {
            values[i] = value;
        }
        return values;
    }

    public static double[] createDoubleArray(int length, double value) {
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = value;
        }
        return values;
    }

    public static int[] combineIntArray(int[] first, int[] second) {
        int[] combine = new int[first.length + second.length];
        int i;
        for (i = 0; i < first.length; i++) {
            combine[i] = first[i];
        }
        for (i = 0; i < second.length; i++) {
            combine[i + first.length] = second[i];
        }
        return combine;
    }


    public static double[] toDoubleArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static int[] toIntegerArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }


    public static float[][] copyOf(final float[][] x, final int newLength) {
        float[][] y = new float[newLength][];
        for (int i = 0; i < y.length; i++) {
            if (x[i] != null) {
                y[i] = Arrays.copyOf(x[i], x[i].length);
            }
        }
        return y;
    }

    /**
     * Assigns a random value to each element of the specified array of doubles.
     */
    public static void fillRandom(final double[] x, final Random rng) {
        for (int i = 0; i < x.length; i++) {
            x[i] = rng.nextDouble();
        }
    }

    /**
     * Returns true if <code>element</code> is in <code>array</code>.
     *
     * @param array
     * @param value
     * @return
     */
    public static boolean isInArray(Object[] array, Object value) {
        for (Object element : array) {
            if (element.equals(value)) {
                return true;
            }
        }

        return false;
    }


    public static boolean isEmpty(int[] arrays) {
        boolean empty = true;
        for (int value : arrays) {
            if (value != 0)
                empty = false;
        }
        return empty;
    }

    public static boolean isEmpty(double[] arrays) {
        boolean empty = true;
        for (double value : arrays) {
            if (!Maths.closeToZero(value))
                empty = false;
        }
        return empty;
    }


    public static boolean containsInvalid(double[] v) {
        for (int i = 0; i < v.length; i++)
            if (Double.isNaN(v[i]) || Double.isInfinite(v[i]))
                return true;
        return false;
    }


    public static double safeAdd(double[] toAdd) {
        // Make sure there are no positive infinities
        double sum = 0;
        for (int i = 0; i < toAdd.length; i++) {
            assert (!(Double.isInfinite(toAdd[i]) && toAdd[i] > 0));
            assert (!Double.isNaN(toAdd[i]));
            sum += toAdd[i];
        }

        return sum;
    }

	/* Methods for filling integer and double arrays (of up to four dimensions) with the given value. */

    public static void set(int[][][][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }

    public static void set(int[][][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }

    public static void set(int[][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }

    public static void set(int[] array, int value) {
        Arrays.fill(array, value);
    }


    public static void set(double[][][][] array, double value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }

    public static void set(double[][][] array, double value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }


    public static double[] arrayMinus(double[] w, double[] v) {
        double result[] = w.clone();
        for (int i = 0; i < w.length; i++) {
            result[i] -= v[i];
        }
        return result;
    }

    public static double[] arrayMinus(double[] result, double[] w, double[] v) {
        for (int i = 0; i < w.length; i++) {
            result[i] = w[i] - v[i];
        }
        return result;
    }

    public static double[] negation(double[] w) {
        double result[] = new double[w.length];
        for (int i = 0; i < w.length; i++) {
            result[i] = -w[i];
        }
        return result;
    }


    public static double[][] outerProduct(double[] w, double[] v) {
        double[][] result = new double[w.length][v.length];
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < v.length; j++) {
                result[i][j] = w[i] * v[j];
            }
        }
        return result;
    }

    public static boolean allPositive(double[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) return false;
        }
        return true;
    }

    /**
     * results = a*W*V
     *
     * @param w
     * @param v
     * @param a weight
     * @return
     */
    public static double[][] weightedouterProduct(double[] w, double[] v, double a) {
        double[][] result = new double[w.length][v.length];
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < v.length; j++) {
                result[i][j] = a * w[i] * v[j];
            }
        }
        return result;
    }


    public static void set(double[][] array, double value) {
        for (int i = 0; i < array.length; i++) {
            set(array[i], value);
        }
    }

    public static void set(double[] array, double value) {
        Arrays.fill(array, value);
    }

    public static void copy(double[][][][] dest, double[][][][] source) {
        for (int i = 0; i < source.length; i++) {
            copy(dest[i], source[i]);
        }
    }


    public static void copy(double[][][] dest, double[][][] source) {
        for (int i = 0; i < source.length; i++) {
            copy(dest[i], source[i]);
        }
    }

    public static void copy(double[][] dest, double[][] source) {
        for (int i = 0; i < source.length; i++) {
            copy(dest[i], source[i]);
        }
    }


    public static void copy(double[] dest, double[] source) {
        System.arraycopy(source, 0, dest, 0, source.length);
    }

    public static void plusEquals(double[][][][] array, double val) {
        for (int i = 0; i < array.length; i++) {
            plusEquals(array[i], val);
        }
    }

    public static void plusEquals(double[][][] array, double val) {
        for (int i = 0; i < array.length; i++) {
            plusEquals(array[i], val);
        }
    }

    /**
     * w = w + a*v
     *
     * @param w
     * @param v
     * @param a
     */
    public static void plusEquals(double[] w, double[] v, double a) {
        for (int i = 0; i < w.length; i++) {
            w[i] += a * v[i];
        }
    }

    public static void plusEquals(double[][] array, double val) {
        for (int i = 0; i < array.length; i++) {
            plusEquals(array[i], val);
        }
    }

    public static void plusEquals(double[] array, double val) {
        for (int i = 0; i < array.length; i++) {
            array[i] += val;
        }
    }

    /**
     * w = w - a*v
     *
     * @param w
     * @param v
     * @param a
     */
    public static void minusEquals(double[] w, double[] v, double a) {
        for (int i = 0; i < w.length; i++) {
            w[i] -= a * v[i];
        }
    }

    /**
     * v = w - a*v
     *
     * @param w
     * @param v
     * @param a
     */
    public static void minusEqualsInverse(double[] w, double[] v, double a) {
        for (int i = 0; i < w.length; i++) {
            v[i] = w[i] - a * v[i];
        }
    }


    public static void scalarMultiplication(double[] w, double v) {
        int w1 = w.length;
        for (int w_i1 = 0; w_i1 < w1; w_i1++) {
            w[w_i1] *= v;
        }
    }

    public static void timesEquals(double[] array, double val) {
        for (int i = 0; i < array.length; i++) {
            array[i] *= val;
        }
    }


    /**
     * sums part of the array -- the sum(array) method is equivalent to
     * sumPart(array, 0, array.length)
     *
     * @param array
     * @param start included in sum
     * @param end   excluded from sum
     * @return
     */
    public static double sumPart(double[] array, int start, int end) {
        double res = 0;
        for (int i = start; i < end; i++) {
            res += array[i];
        }
        return res;
    }



    public static void exponentiate(double[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = Math.exp(a[i]);
        }
    }

    public static void sortDescending(double[] ds) {
        int[] ranks = ArrayUtil.argsort(ds);

        for (int i = 0; i < ranks.length; i++) {
            int position = ranks[i];
            double temp = ds[i];
            ds[i] = ds[position];
            ds[position] = temp;
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        double[] tests = new double[10];
        ArrayUtil.fillRandom(tests, new Random());

        int[] sorts = ArrayUtil.argsort(tests, false);
        for (int i = 0; i < 10; i++) {
            System.out.print(sorts[i] + "  " + tests[sorts[i]] + "\n");
        }

    }

}
