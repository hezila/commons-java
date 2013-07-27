package com.numb3r3.common.optimization.lbfgs;


/**
 * Interface for objective function.
 *
 * @author trung nguyen (trung.ngvan@gmail.com)
 */
public interface ObjectiveFunction {
    /**
     * Computes the value of the objective function at point {@code vars}.
     *
     * @param vars
     * @return
     * @throws InvalidArgumentException if the dimension of the input array does not equal the number of variables
     */
    public double computeFunction(double[] vars) throws IllegalArgumentException;

    /**
     * Computes the value of the gradient at point {@code vars}.
     *
     * @param vars
     * @return
     * @throws InvalidArgumentException if the dimension of the input array does not equal the number of variables
     */
    public double[] computeGradient(double[] vars) throws IllegalArgumentException;
}
