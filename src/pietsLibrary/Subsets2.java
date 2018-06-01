/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */

public class Subsets2 {
    
    public static <T> Map<Integer, List<List<T>>> getSubsets(List<T> list) {
        Map<Integer, List<List<Integer>>> map = new HashMap<>();
        getSubsets(map, list.size() - 1, list.size());
        return mapIndices(list, map);
    }
    
    private static void getSubsets(Map<Integer, List<List<Integer>>> map, int level, int size) {
        if (level == 0) {
            map.computeIfAbsent(0, ArrayList::new).add(new ArrayList<>());
            return;
        }
        getSubsets(map, level - 1, size);
        List<List<Integer>> newList = new ArrayList<>();
        map.put(level, newList);
        map.get(level - 1).stream().forEach(list -> newList.addAll(getNewSubsets(list, size)));   
    }
    
    private static List<List<Integer>> getNewSubsets(List<Integer> list, int size) {
        return IntStream.range(Collections.max(list) + 1, size)
            .mapToObj(i -> {List<Integer> temp = new ArrayList<>(list); temp.add(i); return temp;})
            .collect(toList())
        ;
    }
    
    private static <T> List<T> mapIndexToReal(List<T> list, List<Integer> index) {
        List<T> result = index.stream().map(list::get).collect(toList());
        return result;
    }
    
    private static <T> List<List<T>> mapIndicesToReal(List<T> list, List<List<Integer>> indices) {
        List<List<T>> result = indices.stream().map(index -> mapIndexToReal(list, index)).collect(toList());
        return result;
    }
    
    private static <T> Map<Integer, List<List<T>>> mapIndices(List<T> list, Map<Integer, List<List<Integer>>> map) {
        List<List<Integer>> p = map.values().stream().flatMap(l -> l.stream()).collect(toList());
        List<List<T>> q = mapIndicesToReal(list, p);
        Map<Integer, List<List<T>>> result = q.stream().collect(groupingBy(l -> l.size()));
        return result;
    }
}