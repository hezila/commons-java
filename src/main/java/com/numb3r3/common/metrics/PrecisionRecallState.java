package com.numb3r3.common.metrics;

import com.numb3r3.common.util.Printing;

public class PrecisionRecallState {

	/* correct results */
	private int tp = 0;

	/* unexpected result */
	private int fp = 0;

	/* missing result */
	private int fn = 0;

	/* correct absence of result */
	private int tn = 0;

	public PrecisionRecallState() {

	}

	/**
	 * 
	 * @param tp
	 * 
	 * @param fp
	 * 
	 * @param fn
	 * 
	 */
	public PrecisionRecallState(int tp, int fp, int fn, int tn) {
		this.tp = tp;
		this.fp = fp;
		this.fn = fn;
		this.tn = tn;
	}

	/**
	 * add an instance of true positive - correct result
	 */
	public void incTP() {
		this.tp++;
	}

	public void addTP(int count) {
		this.tp += count;
	}

	/**
	 * add an instance of false positive - unexpected result
	 */

	public void incFP() {
		this.fp++;
	}

	public void addFP(int count) {
		this.fp += count;
	}

	/**
	 * add an instance of false negative - missing result
	 */
	public void incFN() {
		this.fn++;
	}

	public void addFN(int count) {
		this.fn += count;
	}

	/**
	 * add an instance of true negative - correct absence of result
	 */
	public void incTN() {
		this.tn++;
	}

	public void addTN(int count) {
		this.tn += count;
	}

	/**
	 * 
	 * @return the precision, defined as: the number of objects found that have
	 *         a desired property, divided by the total number of objects found.
	 */
	public double precision() {
		int total = tp + fp;

		return tp / (0.0 + total);
	}

	/**
	 * 
	 * @return the recall, defined as the number of objects found that have a
	 *         desired property, divided by the total number of objects with
	 *         that property.
	 */
	public double recall() {
		int total = tp + fn;
		return tp / (0.0 + total);
	}

	/**
	 * 
	 * @return the F1 statistic, which is the harmonic mean of precision and
	 *         recall: 2 * (P * R)/(P + R)
	 */
	public double f1() {
		return 2.0 * (this.precision() * this.recall())
				/ (this.precision() + this.recall());
	}

	public double accuracy() {
		return (this.tp + this.tn + 0.0)
				/ (this.tp + this.tn + this.fp + this.fn);
	}

	public double f1(double precision, double recall) {
		return 2.0 * (precision * recall)/(precision + recall);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		double precision = this.precision();
		double recall = this.recall();
		double accuracy = this.accuracy();
		double f1 = this.f1(precision, recall);
		
		buffer.append("ACC: " + Printing.prettyPrint(accuracy, "###,###.###", 6) + "  ");
		buffer.append("Precision: " + Printing.prettyPrint(precision, "###,###.###", 6) + "  ");
		buffer.append("Recall: " + Printing.prettyPrint(recall, "###,###.###", 6) + " ");
		buffer.append("F1: " + Printing.prettyPrint(f1, "###,###.###",6));
		return buffer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
