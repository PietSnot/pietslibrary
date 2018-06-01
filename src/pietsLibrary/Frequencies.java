/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Sylvia
 */
public class Frequencies {
    
    public static void main(String... args) {
//        Random random = new Random();
//        List<Character> list = new ArrayList<>();
//        for (char c = 'a'; c <= 'z'; c++) {
//            int r = random.nextInt(10);
//            for (int i = 0; i < r; i++) list.add(c);
//        }
//        Map<Character, Integer> map1 = frequencies(list);
//        Map<Integer, List<Character>> map2 = groupByFreq(list);
//        a("" + map1);
//        a("" + map2);
//        a("" + map2.get(1));
//        
//        List<Integer> ints = random.ints(100, 0, 10).boxed().collect(Collectors.toList());
//        Map<Integer, Integer> a1 = frequencies(ints);
//        Map<Integer, List<Integer>> a2 = groupByFreq(ints);
//        Map<Integer, Double> a3 = ints.stream()
//                .collect(Collectors.groupingBy(e -> e, Collectors.averagingInt(e -> e)))
//        ;
//        System.out.println("==========================================");
//        System.out.println("a3: " + a3);
//        System.out.println("==========================================");
//        a("" + a1);
//        a("" + a2);
//        a("" + a2.get(10));
//        a("en nu sortMap");
//        a("" + sortMap(frequencies(list)));
//        a("en nu sortMap met comparator");
//        a("" + sortMap(frequencies(list), Comparator.<Integer>reverseOrder()));
        List<Integer> list = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        for (int i = 1; i <= 20; i++) {
            System.out.println(shuffle(list));
        }
        System.out.println("**************************************");
        for (int i = 1; i <= 20; i++) {
            System.out.println(shuffle2(list));
        }
    }
    
    public static <T> Map<T, Integer> frequencies(List<T> list) {
        return list.stream().collect(Collectors.groupingBy(e -> e, Collectors.summingInt(e -> 1)));
    }
    
    static void a(String s) {
        System.out.println(s);
    }
    
    public static <T> Map<Integer, List<T>> groupByFreq(List<T> list) {
        Map<Integer, List<T>> m2 = new HashMap<>();
        frequencies(list).entrySet().stream().forEach( e -> {
                m2.computeIfAbsent(e.getValue(), ArrayList::new).add(e.getKey());
            }
        );
        return m2;
    }
    
    public static <T> Map<Integer, List<T>> groupByFrequency(List<T> list) {
        return frequencies(list).entrySet().stream().collect(
            groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList()))
        );
    }
    
    public static <T> Map<Integer, List<T>> groupByFreq2(List<T> list) {
        Map<Integer, List<T>> m2 = new HashMap<>();
        for (Map.Entry<T, Integer> entry: frequencies(list).entrySet()) {
            m2.computeIfAbsent(entry.getValue(), ArrayList::new).add(entry.getKey());
        }  
        return m2;
    }
    
    public static <T> Map<Integer, Long> frequencyMap(List<T> list) {
        return frequencies(list).entrySet().stream().collect(
            Collectors.groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, Collectors.counting()))
        );
    }
    
    public static List<Double> discountFactors(double perunage, int duration, boolean prenumerando) {
        double v = 1 / (1 + perunage);
        return DoubleStream
                .iterate(prenumerando ? 1 : v, a -> a * v)
                .limit(duration - 1)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
        ;
    }
    
    public static <K, V extends Comparable<V>> SortedMap<V, List<K>> sortMap(Map<K, V> map) {
        TreeMap<V, List<K>> answer = new TreeMap<>();
        for (Map.Entry<K, V> e: map.entrySet()) {
            List<K> list = answer.computeIfAbsent(e.getValue(), t -> new ArrayList<>());
            list.add(e.getKey());
        }
        return answer;
    }
    
    public static <K, V> SortedMap<V, List<K>> sortMap(Map<K, V> map, Comparator<V> comp) {
        TreeMap<V, List<K>> answer = new TreeMap<>(comp);
        for (Map.Entry<K, V> e: map.entrySet()) {
            answer.computeIfAbsent(e.getValue(), t -> new ArrayList<>()).add(e.getKey());
        }
        return answer;
    }
    
    public static <K, V> SortedMap<V, List<K>> robSpoor(Map<K, V> map) {
        SortedMap<V, List<K>> result = map.entrySet().stream()
            .collect(Collectors.groupingBy(
                Map.Entry::getValue,
                TreeMap::new,
                Collectors.mapping(
                        Map.Entry::getKey,
                        Collectors.toList()
                )
            )
        );
        return result;
    }
    
    public static <K, V> SortedMap<V, List<K>> pietsSort(Map<K, V> map, Comparator<K> compK, Comparator<V> compV) {
        SortedMap<V, List<K>> result = map.entrySet().stream().collect(
            Collectors.groupingBy(
                Map.Entry::getValue,
                () -> new TreeMap<>(compV),
                Collectors.mapping(
                        Map.Entry::getKey,
                        Collectors.toList()
                )
            )
        );
        return result;
    }
    
    public static <K> List<K> shuffle(List<K> list) {
        Random r = new Random();
        Comparator<K> compK = (a, b) -> r.nextInt(11) - 5;
        List<K> result = new ArrayList<>(list);
        result.sort(compK);
        return result;
    }
    
    public static <K> List<K> shuffle2(List<K> list) {
        Random r = new Random();
        List<K> result = new ArrayList<>(list);
        Collections.shuffle(list);
        return result;
    }
}
