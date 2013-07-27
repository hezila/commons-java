package com.numb3r3.common.opt;

/**
 */
public interface Function {
  int dimension();
  double valueAt(double[] x);
}