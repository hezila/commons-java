package com.numb3r3.common.collection;

public interface InvertibleMapper<X, Y> extends Mapper<X,Y> {
    public X unmap(Y input);
}