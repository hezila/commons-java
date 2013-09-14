package com.numb3r3.common.collection;

/**
 * Created with IntelliJ IDEA.
 * User: fwang
 * Date: 9/13/13
 * Time: 9:58 PM
 * from Facebook common package
 */
public interface Mapper<X, Y> {
    public Y map(X input);
}