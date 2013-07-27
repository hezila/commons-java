package com.numb3r3.common.metrics;

import com.numb3r3.common.collection.ArrayUtil;

import java.util.List;

/**
 * from https://github.com/benhamner/Metrics/tree/master/Python/ml_metrics
 *
 * @author numb3r3
 */
public class RankingMetric {

    /**
     * Computes the average precision at k.
     * <p/>
     * This function computes the average prescision at k between two lists of
     * items.
     *
     * @param rels  A list of elements that are to be predicted (order doesn't
     *              matter)
     * @param preds A list of predicted elements (order does matter)
     * @param k     The maximum number of predicted elements
     * @return The average precision at k over the input lists
     */

    private static boolean contains(int[] container, int value) {
        for (int v : container) {
            if (v == value)
                return true;
        }
        return false;
    }

    private static boolean contains(int[] container, int value, int top) {
        for (int i = 0; i < top; i++) {
            int v = container[i];
            if (v == value)
                return true;
        }
        return false;
    }

    public static double average_precision(int[] rels, int[] preds, int k) {
        double score = 0.0;
        double num_hits = 0;

        for (int i = 0; i < k; i++) {
            int candidate = preds[i];

            if (contains(rels, candidate) && !contains(preds, candidate, i)) {
                num_hits++;
                score += num_hits / (i + 1.0);
            }
        }
        return score / Math.min(rels.length, k);

    }

    /**
     * Computes the mean average precision at k.
     * <p/>
     * This function computes the mean average prescision at k between two lists
     * of lists of items.
     *
     * @param rels
     * @param preds
     * @param k
     * @return
     */
    public static double mean_average_precision(List<int[]> rels,
                                                List<int[]> preds, int k) {
        double sum = 0.0;
        for (int i = 0; i < rels.size(); i++) {
            sum += average_precision(rels.get(i), preds.get(i), k);
        }
        return sum / rels.size();
    }


    public static double hit_ratio(int[] rels, int[] preds, int k) {
        double hit = 0.0;
        for (int i = 0; i < k; i++) {
            if (contains(rels, preds[i])) {
                hit++;
            }
        }
        return hit / (k + 0.0);
    }

    public static double rank_precision(int[] rels, int[] preds, int k) {
        double precision = 0.0;
        for (int i = 0; i < k; i++) {
            if (contains(rels, preds[i])) {
                precision += 1.0 / (i + 1.0);
            }
        }
        return precision / (k + 0.0);
    }

    public static double kendall(double[] values, double[] preds) {
        double score = 0.0;
        int count = 0;
        for (int i = 0; i < values.length - 1; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if ((values[i] - values[j]) * (preds[i] - preds[j]) > 0) {
                    score++;
                }
                if (Math.abs(values[i] - values[j]) > 0.0001) {
                    count++;
                }
            }
        }
        if (count > 0) {
            return score / count;
        }
        return -1.0;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        double[] truths = new double[7];
        // RandomUtil.shuffle(truths);
        truths[0] = 4.0;
        truths[1] = 5.0;
        truths[2] = 5.0;
        truths[3] = 5.0;
        truths[4] = 5.0;
        truths[5] = 5.0;
        truths[6] = 3.0;

        int[] rank1 = ArrayUtil.argsort(truths, false);

        double[] preds = new double[7];
        //
        preds[0] = 2.78368;
        preds[1] = 3.22904;
        preds[2] = 2.57254;
        preds[3] = 2.88710;
        preds[4] = 3.40358;
        preds[5] = 3.56623;
        preds[6] = 3.50247;

        int[] rank2 = ArrayUtil.argsort(preds, false);
        rank1[1] = 100;
        rank2[5] = 100;


        System.out.println("MAP@5: "
                + RankingMetric.rank_precision(rank1, rank2, 7));

    }

}
