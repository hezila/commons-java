/**
 * 
 */
package com.numb3r3.common.math;

import com.numb3r3.common.Pair;

import java.io.File;
import java.io.Writer;




/**
 * @author numb3r3
 * 
 */
public interface Matrix {

	public int getRowsNum();

	public int getColumnsNum();

	public int getLength();
	
	public Pair getDims();



	/**
	 * Creates a new matrix where the values of the given vector are the
	 * diagonal values of the matrix.
	 */

	public Matrix dialog();
	
	public Matrix diag();

	public double get(final int row, final int column);


	/**
	 * Get all elements for a given row and the specified columns.
	 * 
	 * @param row
	 * @param indices
	 * @return the 1-dim matrix.
	 */
	public Matrix get(final int row, final int[] indices);

	/**
	 * Get all elements for a given column and the specified rows.
	 * 
	 * @param indices
	 *            the elements in the specified column.
	 * @param column
	 *            the specific column.
	 * @return the 1-dim matrix.
	 */
	public Matrix get(final int[] indices, final int column);

	/**
	 * Get all elements from the specified rows and columns.
	 * 
	 * @param rindices
	 * @param cindices
	 * @return
	 */
	public Matrix get(final int[] rindices, final int[] cindices);

	/**
	 * 
	 * @param row
	 *            the specified row of the matrix.
	 * @return the 1-dim matrix.
	 */
	public Matrix getRow(final int row);

	/**
	 * 
	 * @param column
	 *            the specified column of the matrix.
	 * @return the 1-dim matrix.
	 */
	public Matrix getColumn(final int column);

	/**
	 * Set the value of the elements in the location of [row, column].
	 * 
	 * @param row
	 *            the specified row.
	 * @param column
	 *            the specified column.
	 * @param value
	 *            the value to be added.
	 */
	public void put(final int row, final int column, final double value);

	/**
	 * Find the linear indices of all non-zero elements.
	 */
	public int[] findIndices();

	/**
	 * 
	 * @return Transposed copy of this matrix.
	 */
	public Matrix transpose();

	/**
	 * Copy Matrix m to this. this a is resized if necessary.
	 * 
	 * @param m
	 *            the original matrix
	 */
	public void copy(final Matrix m);

	/**
	 * Returns a duplicate of this matrix. Geometry is the same (including
	 * offsets, transpose, etc.),
	 * 
	 * @return
	 */
	public Matrix dup();

	/**
	 * Swap two columns of a matrix.
	 * 
	 * @param i
	 *            the first column
	 * @param j
	 *            the second column
	 * @return the swaped matrix.
	 */
	public Matrix swapColumns(final int i, final int j);

	/**
	 * Swap two rows of a matrix.
	 * 
	 * @param i
	 *            the first row.
	 * @param j
	 *            the second row.
	 * @return the swaped matrix.
	 */
	public Matrix swapRows(int i, int j);

	public boolean isSameDim(Matrix other);

	/**
	 * Checks whether the matrix is square.
	 */
	public boolean isSquare();

	/**
	 * Converts the matrix to a one-dimensional array of floats.
	 * 
	 * @return
	 */
	public double[] toArray();

	/**
	 * Check whether the matix is a scalar.
	 * 
	 * @return
	 */
	public boolean isScalar();

	/**************************************************************************
	 * Arithmetic Operations
	 */
	/**
	 * Ensures that the result vector has the same length as this. If not,
	 * resizing result is tried, which fails if result == this or result ==
	 * other.
	 */

	/**
	 * Add two matrices (in-place).
	 * 
	 * @param other
	 */
	public void addi(final Matrix other);

	/**
	 * Add two matrices.
	 * 
	 * @param other
	 * @return
	 */
	public Matrix add(final Matrix other);

	/**
	 * Add a scalar to a matrix (in-place).
	 * 
	 * @param scale
	 */
	public void addi(final double scale);

	/**
	 * Add a scalar to a matrix.
	 * 
	 * @param scale
	 * @return
	 */
	public Matrix add(final double scale);

	/**
	 * Subtract a matrix (in-place).
	 * 
	 * @param other
	 */
	public void subi(final Matrix other);

	/**
	 * Subtract a matrix.
	 * 
	 * @param other
	 * @return
	 */
	public Matrix sub(final Matrix other);

	/**
	 * Subtract a scale from the matrix (in-place).
	 * 
	 * @param scale
	 */
	public void subi(final double scale);

	/**
	 * Subtract a scale from the matrix.
	 * 
	 * @param scale
	 * @return
	 */
	public Matrix sub(final double scale);

	/**
	 * Element-wise multiplication (in-place).
	 * 
	 * @param other
	 */
	public void multi(final Matrix other);

	/**
	 * Element-wise multiplication.
	 * 
	 * @param other
	 * @return
	 */
	public Matrix mult(final Matrix other);

	/**
	 * Element-wise multiplication with a scalar (in-place).
	 * 
	 * @param scale
	 */
	public void multi(final double scale);

	/**
	 * Element-wise multiplication with a scalar.
	 * 
	 * @param scale
	 * @return
	 */
	public Matrix mult(final double scale);

	/**
	 * Matrix-matrix multiplication
	 * 
	 * @param other
	 * @return
	 */
	public double dot(final Matrix other);
	
	/**
	 *  Matrix-matrix product
	 * @param other
	 * @return
	 */
	public Matrix product(final Matrix other);

	/**
	 * Elementwise division (in-place).
	 * 
	 * @param other
	 */
	public void divi(final Matrix other);

	/**
	 * Element-wise division.
	 * 
	 * @param other
	 * @return
	 */
	public Matrix div(final Matrix other);

	/**
	 * scale division (in-place)
	 * 
	 * @param scalar
	 */
	public void divi(final double scalar);

	/**
	 * Scalar division.
	 * 
	 * @param scalar
	 * @return
	 */
	public Matrix div(final double scalar);

	/**
	 * Maps zero to 1.0f and all non-zero values to 0.0f (in-place).
	 */
	public void noti();

	/**
	 * Maps zero to 1.0f and all non-zero values to 0.0f.
	 * 
	 * @return
	 */
	public Matrix not();

	/**
	 * Maps zero to 0.0f and all non-zero values to 1.0f (in-place).
	 */
	public void truthi();

	/**
	 * Maps zero to 0.0f and all non-zero values to 1.0f.
	 * 
	 * @return
	 */
	public Matrix truth();
	
	/**
	 * 
	 * @param where
	 * 
	 */
	public void selecti(final Matrix where);

	/**
	 * 
	 * @param where
	 * @return
	 */
	public Matrix select(final Matrix where);

	/** logic operator **/

	/**
	 * Returns the minimal element of the matrix.
	 * 
	 * @return
	 */
	public double min();

	/**
	 * Returns the linear index of the minimal element. If there are more than
	 * one elements with this value, the first one is returned.
	 * 
	 * @return
	 */
	public int argmin();

	/**
	 * Return the maximize element of the matrix.
	 * 
	 * @return
	 */
	public double max();

	/**
	 * Returns the linear index of the maximal element. if there are more than
	 * one elements with this value, the first one is returned.
	 * 
	 * @return
	 */
	public int argmax();

	/**
	 * Computes the sum of all elements of the matrix.
	 * 
	 * @return
	 */
	public double sum();

	/**
	 * Computes the sum of all rows of the matrix.
	 * 
	 * @return
	 */
	public Matrix rsum();

	/**
	 * Compute the sum of the specified row.
	 * 
	 * @param row
	 * @return
	 */
	public double rsum(final int row);

	/**
	 * Computes the sum of all columns of the matrix.
	 * 
	 * @return
	 */
	public Matrix csum();

	/**
	 * Computes the sum of the specified columns.
	 * 
	 * @param column
	 * @return
	 */
	public double csum(final int column);

	/**
	 * Computes the product of all elements of the matrix.
	 * 
	 * @return
	 */
	public double prod();

	/**
	 * Computes the mean value of all elements in the matrix.
	 * 
	 * @return
	 */
	public double mean();

	/**
	 * Computes the mean value of the rows.
	 * 
	 * @return
	 */
	public Matrix rmean();

	/**
	 * Computes the mean value of the specific row.
	 * 
	 * @param row
	 * @return
	 */
	public double rmean(final int row);

	/**
	 * Computes the mean values of the columns.
	 * 
	 * @return
	 */
	public Matrix cmean();
	
	/**
	 * Computes the std values of the columns;
	 * @return
	 */
	public Matrix cstd();

	/**
	 * Computes the mean value of the specific column.
	 * 
	 * @param column
	 * @return
	 */
	public double cmean(final int column);
	
	
	public Matrix rnorm();
	
	public void rnormi();
	
	public Matrix cnorm();
	
	public void cnormi();
	
	public void rdivi(Matrix m);
	
	public Matrix rdiv(Matrix m);
	
	public void cdivi(Matrix m);
	
	public Matrix cdiv(Matrix m);
	

	/**
	 * The Euclidean norm of the matrix as vector, also the Frobenius norm of
	 * the matrix.
	 * 
	 * @return
	 */
	public double norm2();

	/**
	 * The maximum norm of the matrix (maximal absolute value of the elements).
	 * 
	 * @return
	 */
	public double normMax();

	/**
	 * The 1-norm of the matrix as vector (sum of absolute values of elements).
	 * 
	 * @return
	 */
	public double norm1();

	/**
	 * Returns the (euclidean) distance.
	 * 
	 * @return
	 */
	public double euclideanDis(final Matrix other);

	/**
	 * Returns the norm-1 distance.
	 * 
	 * @return
	 */
	public double normDis(final Matrix other);

	/**
	 * Return the norm-2 distance.
	 * 
	 * @return
	 */
	public double norm2Dis(final Matrix other);

	/**
	 * Return a new matrix with all elements sorted.
	 * 
	 * @return
	 */
	public Matrix sort();

	public int[] sortingPermutation();
	
	public Object getData();
	
	public Matrix inverse();
	
	public double det();
	
	
	public Matrix load(File file);
	
	public void dump(Writer writer);
	
	public String dumps();

}
