package com.numb3r3.common.collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

public class ListFilter {
    public static <K> List<K> filter(List<K> input, Predicate<K> predicate) {
        List<K> output = Lists.newArrayList();
        for (K val : input) {
            if (predicate.apply(val)) {
                output.add(val);
            }
        }
        return output;
    }
}
