package com.numb3r3.common.opt;

/**
 * @author Dan Klein
 */
public interface GradientLineSearcher {
  public double[] minimize(DifferentiableFunction function, double[] initial, double[] direction);
}
