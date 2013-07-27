package com.numb3r3.common.opt;
/**
 */
public interface DifferentiableFunction extends Function {
  double[] derivativeAt(double[] x);
}