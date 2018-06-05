/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */

public class Subsets {
    
    public static void main(String... args) {
        List<Integer> list = Arrays.asList(5, 15, 21, 8, 7);
        List<List<Integer>> piet = getSubsets(list);
        piet.stream().forEach(System.out::println);
        Map<Integer, List<List<Integer>>> map = piet.stream().collect(groupingBy(List::size));
        map.entrySet().forEach(System.out::println);
        Map<Integer, Long> map2 = piet.stream().collect(groupingBy(List::size, counting()));
        map2.entrySet().forEach(System.out::println);
    }
    
    public static <T> List<List<T>> getSubsets(List<T> list) {
        Map<Integer, List<List<Integer>>> map = new HashMap<>();
        getSubsets(map, list.size(), list.size());
        return mapIndicesToT(list, map);
    }
    
    private static void getSubsets(Map<Integer, List<List<Integer>>> map, int level, int size) {
        if (level == 0) map.computeIfAbsent(0, ArrayList::new).add(new ArrayList<>());
        else {
            getSubsets(map, level - 1, size);
            List<List<Integer>> newList = new ArrayList<>();
            map.put(level, newList);
            map.get(level - 1).stream().forEach(list -> newList.addAll(getNewSubsets(list, size)));   
        }
    }
    
    private static List<List<Integer>> getNewSubsets(List<Integer> list, int size) {
        return IntStream.range(list.isEmpty() ? 0 : Collections.max(list) + 1, size)
            .mapToObj(i -> {List<Integer> temp = new ArrayList<>(list); temp.add(i); return temp;})
            .collect(toList())
        ;
    }
    
    private static <T> List<T> mapIndexToReal(List<T> list, List<Integer> index) {
        List<T> result = index.stream().map(list::get).collect(toList());
        return result;
    }
    
    private static <T> List<List<T>> mapIndicesToT(List<T> list, Map<Integer, List<List<Integer>>> map) {
        return map.values().stream()
            .flatMap(ll -> ll.stream().map(l -> mapIndexToReal(list, l)))
            .collect(toList())
        ;        
    }
}