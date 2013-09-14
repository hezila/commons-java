package com.numb3r3.common.collection;

import java.util.Map;
import java.util.Map.Entry;

/**
 * maps a Map.Entry<K,V> to V
 *
 * @param <K> key type of entry
 * @param <V> value type of entry
 */
public class EntryValueMapper<K, V> implements Mapper<Entry<K, V>, V> {
    @Override
    public V map(Map.Entry<K, V> input) {
        return input.getValue();
    }
}
