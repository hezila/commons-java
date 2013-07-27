package com.numb3r3.common.concurrent.util;


public interface Reducible<T> extends Duplicatable {
    public void reduce(T instance);
}