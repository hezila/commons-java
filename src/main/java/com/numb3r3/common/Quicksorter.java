package com.numb3r3.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * A utility class that does a quicksort on an {@link ArrayList}. This should be
 * about as fast as {@link Collections#sort(java.util.List)} +/- depending on
 * the complexity of the comparison operation. But it saves the space that
 * {@link Collections#sort(java.util.List)} used to copy into an array and then
 * do a merge sort. That's fine if you want to make sure you can sort a
 * {@link LinkedList} efficiently too, but that's not what we are after here. We
 * want to quick sort in place in an {@link ArrayList} that we can rely on in
 * tight memory situations where copying is not an option.
 */
public class Quicksorter<T extends Comparable<T>> {

	private int smallSubarrayThreshold = 10;

	/**
	 * Quick sort an array. Sorting is done in place.
	 * 
	 * @param array
	 *            the array to sort.
	 */
	public void sort(ArrayList<T> array) {
		sortSubArrayDownToThreshold(array, 0, array.size() - 1);
		insertionSort(array);
	}

	/**
	 * Sort a subarray of the given array. This is the recursive step we use to
	 * sort after partitioning. The indices are inclusive. To sort a whole array
	 * call it with the low index of zero and the high index of the size of the
	 * array minus one, as {@link #sort(ArrayList)} does.
	 * 
	 * @param array
	 *            the array to sort
	 * @param lowIndex
	 *            the index of the first element of the subarray to sort.
	 * @param highIndex
	 *            the index of the last element of the subarray to sort.
	 */
	void sortSubArrayDownToThreshold(ArrayList<T> array, int lowIndex,
			int highIndex) {

		// Is it too small to bother? If so just bail out
		// and leave the sub array unsorted. We will catch
		// it with the final insertion sort.

		if (highIndex - lowIndex < smallSubarrayThreshold) {
			return;
		}

		// Do median of three pivoting. We pick the elements at the low
		// and high index and the middle of the subarray, then put the
		// largest of those three in position highIndex and the second
		// largest in position highIndex - 1. That leaves us ready to
		// scan down from there.

		int midIndex = lowIndex + (highIndex - lowIndex) / 2;

		if (array.get(midIndex).compareTo(array.get(lowIndex)) < 0) {
			swap(array, midIndex, lowIndex);
		}

		if (array.get(highIndex).compareTo(array.get(lowIndex)) < 0) {
			swap(array, highIndex, lowIndex);
		}

		if (array.get(highIndex).compareTo(array.get(midIndex)) < 0) {
			swap(array, highIndex, midIndex);
		}

		swap(array, midIndex, highIndex - 1);
		T pivot = array.get(highIndex - 1);

		// Now run from both ends towards the middle, swapping
		// around the pivot.

		int ii = lowIndex;
		int jj = highIndex - 1;

		while (true) {
			// Scan up from the bottom.
			while (array.get(++ii).compareTo(pivot) < 0) {
				;
			}
			// Scan down from the top.
			while (pivot.compareTo(array.get(--jj)) < 0) {
				;
			}
			if (ii >= jj) {
				break;
			}
			swap(array, ii, jj);
		}

		// Put the pivot back in the middle and then recurse on both sides.

		swap(array, ii, highIndex - 1);

		sortSubArrayDownToThreshold(array, lowIndex, ii - 1);
		sortSubArrayDownToThreshold(array, ii + 1, highIndex);
	}

	/**
	 * Insertion sort is done across the whole array as a final pass.
	 * 
	 * @param array
	 *            the array to sort.
	 */
	void insertionSort(ArrayList<T> array) {
		int size = array.size();

		for (int ii = 1; ii < size; ii++) {
			T tmp = array.get(ii);
			int jj;

			for (jj = ii; jj > 0 && tmp.compareTo(array.get(jj - 1)) < 0; --jj) {
				array.set(jj, array.get(jj - 1));
			}
			array.set(jj, tmp);
		}
	}

	/**
	 * Swap two elements in an array.
	 * 
	 * @param array
	 *            the array
	 * @param index0
	 *            the first index
	 * @param index1
	 *            the second index
	 */
	void swap(ArrayList<T> array, int index0, int index1) {

		T temp = array.get(index0);

		array.set(index0, array.get(index1));
		array.set(index1, temp);
	}

	/**
	 * Get the small sub-array threshold. See
	 * {@link #setSmallSubarrayThreshold(int)} for more info on what this
	 * threshold does.
	 * 
	 * @return the small subarray threshold
	 */
	public int getSmallSubarrayThreshold() {
		return smallSubarrayThreshold;
	}

	/**
	 * Set the small sub-array threshold. When quicksort has partitioned down to
	 * arrays this small it stops. Elements within these subarrays are sorted by
	 * a final insertion sort pass over the whole array.
	 * 
	 * @param smallSubarrayThreshold
	 *            the threshold to set
	 */
	public void setSmallSubarrayThreshold(int smallSubarrayThreshold) {
		this.smallSubarrayThreshold = smallSubarrayThreshold;
	}
	
	public static void main(String[] args) {
		ArrayList<Double> tests = new ArrayList<Double>();
		tests.add(0.2);
		tests.add(0.1);
		tests.add(0.6);
		Quicksorter<Double> sorter = new Quicksorter<Double>();
		sorter.sort(tests);
		for (double value : tests) {
			System.out.println(value);
		}
	}
}
