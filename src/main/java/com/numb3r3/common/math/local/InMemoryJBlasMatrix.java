/**
 *
 */
package com.numb3r3.common.math.local;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.numb3r3.common.ErrorProcessor;
import com.numb3r3.common.Pair;
import com.numb3r3.common.SystemErrorProcessor;
import com.numb3r3.common.Utils;
import com.numb3r3.common.math.Matrix;
import org.jblas.DoubleMatrix;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * @author numb3r3
 */
public class InMemoryJBlasMatrix implements Matrix {

	private DoubleMatrix doubleMatrix = null;

	private ErrorProcessor errorProcessor = null;

	public InMemoryJBlasMatrix(final int rowNum, final int colNum,
			final ErrorProcessor errorProcessor) {
		if (errorProcessor != null) {
			this.errorProcessor = errorProcessor;
		} else {
			this.errorProcessor = new SystemErrorProcessor();
		}

		if (rowNum == 0 && colNum == 0) {
			throw new UnsupportedOperationException(
					"Matrix should have more than zero elements.");

		}

		this.doubleMatrix = DoubleMatrix.zeros(rowNum, colNum);
	}

	public InMemoryJBlasMatrix(final int rowNum, final int colNum) {
		this(rowNum, colNum, null);
	}

	public InMemoryJBlasMatrix() {
		
	}
	
	public static Matrix arrayMatrix(double[] arrays) {
		Matrix m = InMemoryJBlasMatrix.zeros(arrays.length);
		for (int i  = 0; i < arrays.length; i++) {
			m.put(i, 0, arrays[i]);
		}
		return m;
	}

	public InMemoryJBlasMatrix(DoubleMatrix matrix,
			final ErrorProcessor errorProcessor) {
		if (errorProcessor != null) {
			this.errorProcessor = errorProcessor;
		} else {
			this.errorProcessor = new SystemErrorProcessor();
		}
		this.doubleMatrix = matrix;
	}

	public void setData(DoubleMatrix dataMatrix) {
		this.doubleMatrix = dataMatrix;
	}

	public static Matrix zeros(final int size) {
		InMemoryJBlasMatrix matrix = new InMemoryJBlasMatrix();
		matrix.setData(DoubleMatrix.zeros(size));
		return matrix;
	}

	public static Matrix zeros(final int rows, final int cols) {
		InMemoryJBlasMatrix matrix = new InMemoryJBlasMatrix();
		matrix.setData(DoubleMatrix.zeros(rows, cols));
		return matrix;
	}

	/**
	 * Creates an random matrix with elements assinged with values in [0, 1]
	 * 
	 * @param rows
	 *            the number of rows.
	 * @param columns
	 *            the number of columns.
	 * @return the randomness matrix.
	 */
	public static Matrix rand(final int rows, final int columns,
			final ErrorProcessor errorProcessor) {
		return new InMemoryJBlasMatrix(DoubleMatrix.rand(rows, columns),
				errorProcessor);
	}

	public static Matrix rand(final int length,
			final ErrorProcessor errorProcessor) {
		return InMemoryJBlasMatrix.rand(length, 1, errorProcessor);
	}

	/**
	 * Create matrix with normally distributed random values.
	 * 
	 * @param rows
	 *            the number of rows.
	 * @param columns
	 *            the number of columns.
	 * @return the randomess matrix with gaussian distribution.
	 */
	public static Matrix randn(final int rows, final int columns) {
		return new InMemoryJBlasMatrix(DoubleMatrix.randn(rows, columns), null);
	}

	public static Matrix randn(final int length) {
		return InMemoryJBlasMatrix.randn(length, 1);
	}

	public static Matrix ones(final int rows, final int columns) {
		return new InMemoryJBlasMatrix(DoubleMatrix.ones(rows, columns), null);
	}

	public static Matrix ones(final int length) {
		return InMemoryJBlasMatrix.ones(length, 1);
	}

	public static Matrix eye(final int n) {
		return new InMemoryJBlasMatrix(DoubleMatrix.eye(n), null);
	}

	@Override
	public Pair getDims() {
		return new Pair(this.getRowsNum(), this.getColumnsNum());
	}

	@Override
	public DoubleMatrix getData() {
		return this.doubleMatrix;
	}

	private int changeSign(int i) {
		if (i % 2 == 0)
			return 1;
		else
			return -1;
	}

	private Matrix createSubMatrix(int excluding_row, int excluding_col) {
		Matrix mat = new InMemoryJBlasMatrix(this.getRowsNum() - 1,
				this.getColumnsNum() - 1, this.errorProcessor);
		int r = -1;
		for (int i = 0; i < this.getRowsNum(); i++) {
			if (i == excluding_row)
				continue;
			r++;
			int c = -1;
			for (int j = 0; j < this.getColumnsNum(); j++) {
				if (j == excluding_col)
					continue;
				mat.put(r, ++c, this.get(i, j));
			}
		}
		return mat;
	}

	@Override
	public double det() {
		if (!this.isSquare()) {
			System.out.println("Error: not squre matrix.");
			System.exit(0);
		}
		if (this.getLength() == 1) {
			return this.get(0, 0);
		}
		if (this.getLength() == 2) {
			return this.get(0, 0) * this.get(1, 1) - this.get(0, 1)
					* this.get(1, 0);
		}

		double sum = 0.0;
		for (int i = 0; i < this.getColumnsNum(); i++) {
			sum += changeSign(i) * this.get(0, i)
					* this.createSubMatrix(0, i).det();
		}
		return sum;
	}

	private Matrix cofactor() {
		Matrix mat = new InMemoryJBlasMatrix(this.getRowsNum(),
				this.getColumnsNum(), this.errorProcessor);
		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				mat.put(i, j,
						changeSign(i) * changeSign(j)
								* createSubMatrix(i, j).det());
			}
		}

		return mat;
	}

	@Override
	public Matrix inverse() {
		return this.cofactor().transpose().mult(1.0 / this.det());

	}

	@Override
	public int getRowsNum() {
		return this.doubleMatrix.getRows();
	}

	@Override
	public int getColumnsNum() {
		return this.doubleMatrix.getColumns();
	}

	@Override
	public int getLength() {
		return this.doubleMatrix.getLength();
	}

	@Override
	public Matrix dialog() {
		if (this.isSquare()) {
			return new InMemoryJBlasMatrix(DoubleMatrix.diag((DoubleMatrix) this.diag().getData()), null);
		} else {
			return new InMemoryJBlasMatrix(DoubleMatrix.diag(this.getData()),null);
		}
	}
	
	@Override
	public Matrix diag() {
		return new InMemoryJBlasMatrix(this.getData().diag(), null);
	}

	@Override
	public double get(int row, int column) {
		return this.doubleMatrix.get(row, column);
	}

	@Override
	public Matrix get(int row, int[] indices) {
		return new InMemoryJBlasMatrix(this.doubleMatrix.get(row, indices),
				null);
	}

	@Override
	public Matrix get(int[] indices, int column) {
		return new InMemoryJBlasMatrix(this.doubleMatrix.get(indices, column),
				null);
	}

	@Override
	public Matrix get(int[] rindices, int[] cindices) {
		return new InMemoryJBlasMatrix(
				this.doubleMatrix.get(rindices, cindices), null);
	}

	@Override
	public Matrix getRow(int row) {
		if (row > this.getRowsNum()) {
			errorProcessor.error("the row is out of boundary.");
			throw new UnsupportedOperationException(
					"the row is out of boundary.");
		} else {
			Matrix dump = this.dup();
			return new InMemoryJBlasMatrix(
					((DoubleMatrix) dump.getData()).getRow(row), null);
		}

	}

	@Override
	public Matrix getColumn(int column) {
		Matrix dump = this.dup();
		return new InMemoryJBlasMatrix(
				((DoubleMatrix) dump.getData()).getColumn(column), null);
	}

	@Override
	public void put(int row, int column, double value) {
		if (row > this.getRowsNum() - 1 || column > this.getColumnsNum() - 1) {
			errorProcessor.error("The row/column is out of bounds.");
			return;
		}
		this.doubleMatrix.put(row, column, value);
	}

	@Override
	public int[] findIndices() {
		return this.doubleMatrix.findIndices();
	}

	@Override
	public Matrix transpose() {
		return new InMemoryJBlasMatrix(this.doubleMatrix.transpose(),
				this.errorProcessor);
	}

	@Override
	public void copy(Matrix m) {
		if (m.getRowsNum() == this.getRowsNum()
				&& m.getColumnsNum() == this.getColumnsNum()) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					this.put(i, j, m.get(i, j));
				}
			}
		} else {
			errorProcessor.error("The matrices should have same dimensions.");
		}
	}

	@Override
	public Matrix dup() {
		return new InMemoryJBlasMatrix(this.getData().dup(),
				this.errorProcessor);
	}

	@Override
	public Matrix swapColumns(int i, int j) {
		return new InMemoryJBlasMatrix(this.doubleMatrix.swapColumns(i, j),
				this.errorProcessor);
	}

	@Override
	public Matrix swapRows(int i, int j) {
		return new InMemoryJBlasMatrix(this.doubleMatrix.swapRows(i, j),
				this.errorProcessor);
	}

	@Override
	public boolean isSquare() {
		return this.doubleMatrix.isSquare();
	}

	@Override
	public double[] toArray() {
		return this.doubleMatrix.toArray();
	}

	@Override
	public boolean isScalar() {
		return this.doubleMatrix.isScalar();
	}

	public boolean isSameDim(Matrix other) {
		if (this.getRowsNum() == other.getRowsNum()
				&& this.getColumnsNum() == other.getColumnsNum()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addi(Matrix other) {
		if (this.isSameDim(other)) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					this.put(i, j, this.get(i, j) + other.get(i, j));
				}
			}
		} else {
			// System.out.println(this + "\n" + other + "\n");
			errorProcessor
					.error("The two matrices should have same dimensions.");
		}

	}

	@Override
	public Matrix add(Matrix other) {
		Matrix dump = this.dup();
		dump.addi(other);
		return dump;
	}

	@Override
	public void addi(double scale) {
		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				this.put(i, j, this.get(i, j) + scale);
			}
		}
	}

	@Override
	public Matrix add(double scale) {
		Matrix dump = this.dup();
		dump.addi(scale);
		return dump;
	}

	@Override
	public void subi(Matrix other) {
		if (this.isSameDim(other)) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					this.put(i, j, this.get(i, j) - other.get(i, j));
				}
			}
		} else {
			errorProcessor
					.error("the two matrices should have same dimensions.");
		}
	}

	@Override
	public Matrix sub(Matrix other) {
		if (this.isSameDim(other)) {
			Matrix dump = this.dup();
			dump.subi(other);
			return dump;
		} else {
			errorProcessor
					.error("the two matrices should have same dimensions.");
			return null;
		}
	}

	@Override
	public void subi(double scale) {
		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				this.put(i, j, this.get(i, j) - scale);
			}
		}

	}

	@Override
	public Matrix sub(double scale) {
		Matrix dump = this.dup();
		dump.subi(scale);
		return dump;
	}

	@Override
	public void multi(Matrix other) {
		if (this.isSameDim(other)) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					this.put(i, j, this.get(i, j) * other.get(i, j));
				}
			}
		} else {
			errorProcessor
					.error("the two matrices should have same dimensions.");
		}

	}

	@Override
	public Matrix mult(Matrix other) {
		Matrix dump = this.dup();
		dump.multi(other);
		return dump;
	}

	@Override
	public void multi(double scale) {
		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				this.put(i, j, this.get(i, j) * scale);
			}
		}
	}

	@Override
	public Matrix mult(double scale) {
		Matrix dump = this.dup();
		dump.multi(scale);
		return dump;
	}

	@Override
	public double dot(final Matrix other) {
		return this.getData().dot((DoubleMatrix) other.getData());
	}

	@Override
	public Matrix product(final Matrix other) {
		if (this.getColumnsNum() == other.getRowsNum()) {
			Matrix temp = new InMemoryJBlasMatrix(this.getRowsNum(),
					other.getColumnsNum(), errorProcessor);
			// System.out.println(temp + "\n");
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < other.getColumnsNum(); j++) {
					// System.out.println(this.getRow(i) + "\t" +
					// other.getColumn(j) + "\n");
					// System.out.println(i + ":" + j + "\t" +
					// this.getRow(i).dot(other.getColumn(j)));
					// temp.put(i, j, 0.0);
					temp.put(i, j, this.getRow(i).dot(other.getColumn(j)));
				}
			}
			return temp;
		} else {
			errorProcessor
					.error("the two matrices should have matched dimensions.");
			return null;
		}
	}

	@Override
	public void divi(Matrix other) {
		this.getData().divi((DoubleMatrix) other.getData());

	}

	@Override
	public Matrix div(Matrix other) {
		if (this.isSameDim(other)) {
			Matrix dump = new InMemoryJBlasMatrix(this.getData().div(
					(DoubleMatrix) other.getData()), errorProcessor);
			return dump;
		} else {
			errorProcessor
					.error("the two matrices should have same dimensions.");
			return null;
		}

	}

	@Override
	public void divi(double scalar) {
		if (!Utils.isZero(scalar)) {
			this.getData().divi(scalar);
		} else {
			errorProcessor
					.error("the operation is illegal. (the division should not to be zero)");
		}

	}

	@Override
	public Matrix div(double scalar) {
		if (!Utils.isZero(scalar)) {
			Matrix dump = this.dup();
			((DoubleMatrix) dump.getData()).divi(scalar);
			return dump;
		} else {
			errorProcessor
					.error("the operation is illegal. (the division should not to be zero)");
			return null;
		}
	}

	/**
	 * Maps zero to 1.0 and all non-zero values to 0.0 (in-place).
	 */
	@Override
	public void noti() {
		this.getData().noti();

	}

	@Override
	public Matrix not() {
		Matrix dump = this.dup();
		((DoubleMatrix) dump.getData()).noti();
		return dump;
	}

	/**
	 * Maps zero to 0.0 and all non-zero values to 1.0 (in-place).
	 */
	@Override
	public void truthi() {
		this.getData().truthi();
	}

	@Override
	public Matrix truth() {
		Matrix dump = this.dup();
		((DoubleMatrix) dump.getData()).truthi();
		return dump;
	}

	@Override
	public void selecti(final Matrix where) {
		this.getData().selecti((DoubleMatrix) where.getData());
	}

	@Override
	public Matrix select(final Matrix where) {
		Matrix dump = this.dup();
		((DoubleMatrix) dump.getData()).select((DoubleMatrix) where.getData());
		return dump;
	}

	@Override
	public double min() {
		return this.getData().min();
	}

	@Override
	public int argmin() {
		return this.getData().argmin();
	}

	@Override
	public double max() {
		return this.getData().max();
	}

	@Override
	public int argmax() {
		return this.getData().argmax();
	}

	@Override
	public double sum() {
		return this.getData().sum();
	}

	@Override
	public Matrix rsum() {
		Matrix matrix = new InMemoryJBlasMatrix(this.getRowsNum(), 1,
				errorProcessor);
		for (int i = 0; i < this.getRowsNum(); i++) {
			matrix.put(i, 0, this.rsum(i));
		}
		return matrix;
	}

	@Override
	public double rsum(int row) {
		if (row < this.getRowsNum()) {
			double temp = 0.0;
			for (int j = 0; j < this.getColumnsNum(); j++) {
				temp += this.get(row, j);
			}
			return temp;
		} else {
			errorProcessor.error("Row overflow.");
			throw new UnsupportedOperationException("Row overflow.");
		}
	}

	@Override
	public Matrix csum() {
		Matrix matrix = new InMemoryJBlasMatrix(1, this.getColumnsNum(),
				errorProcessor);
		for (int j = 0; j < this.getColumnsNum(); j++) {
			matrix.put(0, j, this.csum(j));
		}
		return matrix;
	}

	@Override
	public double csum(int column) {
		if (column < this.getColumnsNum()) {
			double temp = 0.0;
			for (int i = 0; i < this.getRowsNum(); i++) {
				temp += this.get(i, column);
			}
			return temp;
		} else {
			errorProcessor.error("Column overflow.");
			throw new UnsupportedOperationException("Column overflow.");
		}

	}

	@Override
	public double prod() {
		return this.getData().prod();
	}

	@Override
	public double mean() {
		return this.getData().mean();
	}

	@Override
	public Matrix rmean() {
		Matrix matrix = this.rsum();
		matrix.divi(this.getColumnsNum());
		return matrix;
	}

	@Override
	public double rmean(int row) {
		return this.rsum(row) / this.getColumnsNum();
	}

	@Override
	public Matrix cmean() {
		Matrix matrix = this.csum();
		matrix.divi(this.getRowsNum());
		return matrix;
	}

	@Override
	public Matrix cstd() {
		Matrix mean = this.cmean();
		Matrix std = zeros(1, this.getColumnsNum());
		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				double value = this.get(i, j);
				double old = std.get(0, j);
				std.put(0, j, old + (value - mean.get(0, j))
								* (value - mean.get(0, j)));
			}
		}
		std.divi(this.getRowsNum());
		for (int j = 0; j < this.getColumnsNum(); j++) {
			double value = Math.sqrt(std.get(0, j));
			std.put(0, j, value);
		}
		return std;
	}

	@Override
	public double cmean(int column) {
		return this.csum(column) / this.getRowsNum();
	}

	@Override
	public double norm2() {
		return this.getData().norm2();
	}

	/**
	 * The maximum norm of the matrix (maximal absolute value of the elements).
	 * 
	 * @return
	 */
	@Override
	public double normMax() {
		return this.getData().normmax();
	}

	@Override
	public double norm1() {
		return this.getData().norm1();
	}

	@Override
	public double euclideanDis(final Matrix other) {
		return this.getData().distance2((DoubleMatrix) other.getData());
	}

	@Override
	public double normDis(final Matrix other) {
		return this.getData().distance1((DoubleMatrix) other.getData());
	}

	@Override
	public double norm2Dis(final Matrix other) {
		return this.euclideanDis(other);
	}

	@Override
	public Matrix sort() {
		return new InMemoryJBlasMatrix(this.getData().sort(), errorProcessor);
	}

	@Override
	public int[] sortingPermutation() {
		return this.getData().sortingPermutation();
	}

	/**
	 * Generate string representation of the matrix.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		s.append("[");

		for (int i = 0; i < this.getRowsNum(); i++) {
			for (int j = 0; j < this.getColumnsNum(); j++) {
				s.append(get(i, j));
				if (j < this.getColumnsNum() - 1) {
					s.append(", ");
				}
			}
			if (i < this.getRowsNum() - 1) {
				s.append("; \n");
			}
		}

		s.append("]");

		return s.toString();
	}

	@Override
	public Matrix load(File file) {
		try {
			Matrix matrix = null;
			int index = 0;
			for (String line : Files.readLines(file, Charsets.UTF_8)) {
				if (index == 0) {
					StringTokenizer token = new StringTokenizer(line, "\t");
					int m = Integer.parseInt(token.nextToken());
					int n = Integer.parseInt(token.nextToken());
					matrix = new InMemoryJBlasMatrix(m, n);
				} else {
					int j = 0;
					for (String item : Splitter.on(",").trimResults()
							.omitEmptyStrings().split(line)) {
						matrix.put(index - 1, j, Double.parseDouble(item));
						j++;
					}
				}
				index++;
			}
			return matrix;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dump(Writer writer) {
		String dump_text = this.dumps();
		try {
			writer.write(dump_text);
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String dumps() {
		String text = "" + this.getRowsNum() + "\t" + this.getColumnsNum()
				+ "\n";
		for (int i = 0; i < this.getRowsNum(); i++) {
			int j;
			for (j = 0; j < this.getColumnsNum() - 1; j++) {
				text += this.get(i, j) + ", ";
			}
			text += this.get(i, j) + "\n";
		}
		return text;
	}

	@Override
	public void rdivi(Matrix m) {
		if (this.getRowsNum() == m.getRowsNum() && m.getColumnsNum() == 1) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					assert m.get(i, 0) == 0.0;
					this.put(i, j, this.get(i, j) / m.get(i, 0));
				}
			}
		} else {
			System.err.println("Error: matrix dimension does not matches");
			System.exit(-1);
		}
	}

	@Override
	public Matrix rdiv(Matrix m) {
		Matrix copy = new InMemoryJBlasMatrix(this.getRowsNum(),
				this.getColumnsNum());
		copy.copy(this);
		if (this.getRowsNum() == m.getRowsNum() && m.getColumnsNum() == 1) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					assert m.get(i, 0) == 0.0;
					copy.put(i, j, copy.get(i, j) / m.get(i, 0));
				}
			}
			return copy;
		} else {
			System.err.println("Error: matrix dimension does not matches");
			return null;
		}
	}

	@Override
	public void cdivi(Matrix m) {
		if (this.getColumnsNum() == m.getColumnsNum() && m.getRowsNum() == 1) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					assert m.get(0, i) == 0.0;
					this.put(i, j, this.get(i, j) / m.get(0, j));
				}
			}
		}
	}

	@Override
	public Matrix cdiv(Matrix m) {
		Matrix copy = new InMemoryJBlasMatrix(this.getRowsNum(),
				this.getColumnsNum());
		copy.copy(this);
		if (this.getColumnsNum() == m.getColumnsNum() && m.getRowsNum() == 1) {
			for (int i = 0; i < this.getRowsNum(); i++) {
				for (int j = 0; j < this.getColumnsNum(); j++) {
					assert m.get(0, i) == 0.0;
					copy.put(i, j, copy.get(i, j) / m.get(0, j));
				}
			}
			return copy;
		} else {
			System.err.println("Error: matrix dimension does not matches");
			return null;
		}
	}

	@Override
	public Matrix rnorm() {
		Matrix rsum = this.rsum();
		return this.rdiv(rsum);
	}

	@Override
	public void rnormi() {
		Matrix rsum = this.rsum();
		this.rdivi(rsum);
	}

	@Override
	public Matrix cnorm() {
		Matrix csum = this.csum();
		return this.cdiv(csum);
	}

	@Override
	public void cnormi() {
		Matrix csum = this.csum();
		this.cdivi(csum);
	}

	public static void main(String[] args) {

		// Matrix mat = InMemoryJBlasMatrix.eye(3);
		// System.out.println(mat);
		// System.out.println();

		// Matrix zeros = InMemoryJBlasMatrix.zeros(10);
		// System.out.println(zeros + "\n");

		final Matrix ones = InMemoryJBlasMatrix.ones(3, 3);
		ones.put(0, 0, 0.5);
		ones.put(0, 1, 0.3);
		ones.put(1, 1, 0.4);
		
		System.out.println("ONES: " + ones.dialog());
		System.out.println("SUB: " + ones.sub(InMemoryJBlasMatrix.eye(3)));
		System.out.println("EYE" + InMemoryJBlasMatrix.eye(5));

		Matrix m = InMemoryJBlasMatrix.rand(5, null);
		m = m.product(m.transpose());
		System.out.println(m + "\n");
		
		
		System.out.println(m.dialog());
		System.out.println(m.diag());
		

	}

}
