package com.numb3r3.common.opt;

import org.jblas.DoubleMatrix;

public abstract class DifferentiableMatrixFunction {

	public DoubleMatrix valueAt(DoubleMatrix M){ return null; }

	public DoubleMatrix derivativeAt(DoubleMatrix M){ return null; }
}