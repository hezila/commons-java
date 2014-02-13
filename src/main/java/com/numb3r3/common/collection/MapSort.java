package com.numb3r3.common.collection;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fwang on 12/13/13.
 */
public class MapSort {
    public static Map sortByValue(Map unsortedMap){
        final Ordering<String> reverseValuesAndNaturalKeysOrdering =
                Ordering.natural().reverse().nullsLast().onResultOf(Functions.forMap(unsortedMap, null)) // natural for values
                        .compound(Ordering.natural()); // secondary - natural ordering of keys


        return ImmutableSortedMap.copyOf(unsortedMap, reverseValuesAndNaturalKeysOrdering);
    }
    public static Map sortByKey(Map unsortedMap){
        Map sortedMap = new TreeMap();
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public static void main(String[] argv) {
        Map<String, Double> map = Maps.newHashMap();
        map.put("k1", 1.0);
        map.put("k5", 2.0);
        map.put("k3", 3.0);
        map.put("k4", 0.0);
        List<String> rs = Lists.newArrayList();
        rs.addAll(sortByValue(map).keySet());
        System.out.println(rs);
    }


}
