/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 * @param <T>
 */
public class SummaryStats<T extends Number> {
    
    List<T> list = new ArrayList<>();
    
    public SummaryStats() {}
    
    public SummaryStats(List<T> input) {
        list.addAll(input);
    }
    
    public void add(T number) {
        list.add(number);
    }
    
    public double average() {
        if (list.size() == 0) throw new RuntimeException("no data!");
        return list.stream().mapToDouble(i -> i.doubleValue()).average().getAsDouble();
    }
    
    public double variance() {
        if (list.size() < 2) throw new RuntimeException("too little data!!!");
        var avg = average();
        var EX2 = list.stream().mapToDouble(i -> i.doubleValue()).map(i -> i * i).average().getAsDouble();
        return EX2 - avg * avg;
    }
    
    public double stdev() {
        return Math.sqrt(variance());
    }
    
    public int getNrOfObservations() {
        return list.size();
    }
    
    public List<T> getData() {
        return list.stream().collect(toList());
    }
    
    public double max() {
        if (list.isEmpty()) throw new RuntimeException("No data available!!!");
        return list.stream().mapToDouble(i -> i.doubleValue()).max().getAsDouble();
    }
    
    public double min() {
        if (list.isEmpty()) throw new RuntimeException("No data available!!!");
        return list.stream().mapToDouble(i -> i.doubleValue()).min().getAsDouble();
    }
}