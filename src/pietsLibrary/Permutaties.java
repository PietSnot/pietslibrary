/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Permutaties {
    
    /**
     * use: Permutaties.getPermutations(List<T> list)
     * 
     * Piet Souris, Den Haag, 2018-05-30
     * @param args
     */
    
    public static void main(String... args) {
        List<String> list = Arrays.asList("aap", "noot", "mies", "aap");
        List<List<String>> perms = getPermutations(list);
        perms.forEach(System.out::println);
    }
    
    //****************************************************************
    // public methods
    //****************************************************************
    public static <T> List<List<T>> getPermutations(List<T> list) {
        List<List<Integer>> hulp = permutaties(list.size());
        return hulp.stream().map(intlist -> permuteer(list, intlist)).collect(toList());
    }
    
    //****************************************************************
    // private methods
    //****************************************************************
    private static List<List<Integer>> permutaties(int N) {
        List<Integer> original = IntStream.range(0, N).boxed().collect(toList());
        List<List<Integer>> result = new ArrayList<>();
        permutaties(result, original, new ArrayList<>());
        return result;
    }
    
    private static void permutaties(List<List<Integer>> total, List<Integer> original, List<Integer> currentList) {
        if (currentList.size() == original.size()) total.add(new ArrayList<>(currentList));
        else original.stream()
            .filter(i -> !currentList.contains(i))
            .forEach(i -> {
                currentList.add(i); 
                permutaties(total, original, currentList);
                currentList.remove(currentList.size() - 1);
        });
    }
    
    private static <T> List<T> permuteer(List<T> list, List<Integer> permutaties) {
        return permutaties.stream().map(list::get).collect(toList());
    }
}