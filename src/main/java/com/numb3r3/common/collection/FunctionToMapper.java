package com.numb3r3.common.collection;

/**
 * Created with IntelliJ IDEA.
 * User: fwang
 * Date: 9/13/13
 * Time: 10:00 PM
 * from Facebook common package
 */
import com.google.common.base.Function;

public class FunctionToMapper<X, Y> implements Mapper<X, Y> {
    private final Function<X, Y> function;

    public FunctionToMapper(Function<X, Y> function) {
        this.function = function;
    }

    @Override
    public Y map(X input) {
        return function.apply(input);
    }
}
