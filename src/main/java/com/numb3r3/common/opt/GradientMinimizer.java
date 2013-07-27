package com.numb3r3.common.opt;

/**
 * @author Dan Klein
 */
public interface GradientMinimizer {
    double[] minimize(DifferentiableFunction function, double[] initial,
                      double tolerance);
}