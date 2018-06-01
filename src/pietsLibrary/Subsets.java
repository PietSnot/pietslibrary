package pietsLibrary;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 * class Subsets is a class that is intended for producing subsets of a 
 * given list. To use this class, just create an instance of this class.
 * So, start with, say:
 * 
 * Subsets subs = new Subsets();
 * 
 * Available methods are then: subs.
 * subsets(List<E> list> , giving all the subsets of 'list', <E> is a type 
 *                         parameter
 * subsetsReduce(List<E> list, ...), applies a reduction to each of the
 *                                   subsets and returns a list of these 
 *                                   'reduced' values
 *
 * @author Piet, 2015-06-20
 */
public class Subsets {
    
    /**
     * This class has two static methods: one gives the subsets of a
     * given set, and the other can apply a reduction on these subsets
     * @param args the command line arguments, if any, can be left empty
     */
    public static void main(String... args) {
//        Subsets s = new Subsets();
//        List<String> strings = Arrays.asList("aap", "noot", "mies", "wim", "zus", "jet");
//        List<List<String>> outcome = s.subsets(strings);
//        outcome.forEach(e ->System.out.println(e));
//        List<Integer> sumlist = s.subsetsReduce(strings, e -> e.length(), 0, (a,b) -> a + b);
//        List<Integer> prodlist = s.subsetsReduce(strings, e -> e.length(), 1, (a,b) -> a * b);
//        System.out.println(sumlist);
//        System.out.println(prodlist);
        int[] a = {1, 1, 2, 2, 4, 6};
        List<Integer> list = new ArrayList<>();
        for (int x: a) list.add(x);
        Set<List<Integer>> set = Subsets.permutations(list);
        List<List<Integer>> poes = set.stream().map(e -> e.subList(0, 1)).collect(Collectors.toList());
        Map<List<Integer>, Integer> map = Frequencies.frequencies(poes);
        Map<Integer, List<List<Integer>>> map2 = Frequencies.groupByFreq(poes);
        map.entrySet().forEach(e -> System.out.println(e.toString()));
        map2.entrySet().forEach(e -> System.out.println(e.toString()));
        
    }
    
    /**
     * this method creates a list of all the subsets of the input list 'list'.
     * This list of subsets includes the empty list, and so the returned list
     * has cardinality (number of elements) equal to 2 ^ list.size().
     * @param <E> the type of the input list
     * @param list the list of which all subsets are to be created. Of course,
     * all implementations of List are allowed as parameter here
     * @return a list of all the subsets of the input list.
     */
    public static <E> List<List<E>> subsets(List<E> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("inputlist cannot be null or empty!");
        }
        List<List<E>> mainlist = new ArrayList<>();
        recursion(list.size(), 0, mainlist, new ArrayList<>(), list);
        return mainlist;
    }
    
    /**
     * This method applies a reduction to all of the subsets of the input list.
     * It then returns a list of all thes 'reduced' values.
     * For instance, say we have tis list: ["a", "bb"]. We then have this list
     * of all the subsets:
     * ["", ["a"], ["bb"], ["a", "bb"]], four in total.
     * Say that we want to know the total length of each of the subsets, then
     * this method woud produce the list [0, 1, 2, 3]. The usage would be:
     * 
     * -----------------------------------------------------------------------
     * List<String> list = Arrays.asList("a", "bb");
     * Subsets subs = new Subsets();
     * List<Integer> result = 
     *     subs.subsetsReduce(list, e -> e.length(), 0, (a, b) -> a + b);
     * -----------------------------------------------------------------------
     * 
     * @param T the type of the reduced values
     * @param E the type of th elements of the input list
     * @param list the input list
     * @param f a function that maps an element of type E to type T
     * @param initvalue in the reduction of a subset, this is the T to start with
     * @param t a function that does the reduction. See the Stream API, for more
     * information regarding stream.reduce
     * @return a list<T> of reduced values
     */
    public static <T, E> List<T> subsetsReduce(
            List<E> list, Function<E, T> f, T initvalue, BinaryOperator<T> t) 
    {
        List<List<E>> templist = subsets(list);
        List<T> temp = 
            templist.stream()
                .map(e -> compress(e, f, initvalue, t))
                .collect(Collectors.toList())
            ;    
        return temp;    
    }
    
    /**
     * a help function that creates the subsets, by using recursion.
     * @param <E> the type of the elements
     * @param length the size of the input list
     * @param level
     * @param mainlist
     * @param templist
     * @param original 
     */
    private static <E> void recursion
        (int length, int level, List<List<E>> mainlist, List<E> templist, List<E> original) 
    {
        if (level == length) {
            mainlist.add(templist);
            return;
        }
        List<E> temp = new ArrayList<>(templist);
        recursion(length, level + 1, mainlist, templist, original);
        templist = temp;
        templist.add(original.get(level));
        recursion(length, level + 1, mainlist, templist, original);
    }
    
    /**
     * 
     * @param T
     * @param E
     * @param list
     * @param f
     * @param init
     * @param t
     * @return 
     */
    private static <T, E> T compress(List<E> list, Function<E, T> f, T init, BinaryOperator<T> t) {
        T temp =
            list.stream()
            .map(e -> f.apply(e))
            .reduce(init, (a, b) -> t.apply(a, b))
        ;
        return temp;
    }
    
    private static <T> Set<List<T>> permutations(List<T> list) {
        List<T> original = new ArrayList<>(list);
        List<T> sofar = new ArrayList<>();
        Set<List<T>> answer = new HashSet<>();
        permHelp(original, sofar, answer);
        return answer;
    }
    
    private static <T> void permHelp(List<T> original, List<T> sofar, Set<List<T>> answer) {
        if (original.isEmpty()) {
            answer.add(sofar);
            return;
        }
        
        for (T t: original) {
            ArrayList<T> temp = new ArrayList<>(original);
            ArrayList<T> flup = new ArrayList<>(sofar);
            temp.remove(t);
            flup.add(t);
            permHelp(temp, flup, answer);
        }
    }
    
    public static <T> Map<Integer, List<List<T>>> deelverzamelingen(List<T> list) {
        Map<Integer, List<List<Integer>>> map = new HashMap<>();
        getSubsets(map, list.size(), list.size());
        return mapIndicesToReal(map);
    }
    
    private static void getSubsets(Map<Integer, List<List<Integer>>> map, int level, int size) {
        if (level == 0) {
            map.computeIfAbsent(0, ArrayList::new).add(new ArrayList<>());
            return;
        }
        getSubsets(map, level - 1, size);
        List<List<Integer>> newList = new ArrayList<>();
        map.put(level, newList);
        for (List<Integer> list: map.get(level - 1)) {
            newList.addAll(getNewSubsets(list, size));   
        }
    }
    
    private static List<List<Integer>> getNewSubsets(List<Integer> list, int size) {
        int max = Collections.max(list);
        return IntStream.range(max + 1, size)
            .mapToObj(i -> {List<Integer> temp = new ArrayList<>(list); temp.add(i); return temp; })
            .collect(toList())
        ;
    }
    
    private static <T> List<T> mapIndicesToRealList(List<T> list, List<Integer> indices) {
        return indices.stream().map(list::get).collect(toList());
    }
}
