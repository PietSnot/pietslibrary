/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsLibrary;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

/**
 *
 * @author Sylvia
 */
public class MatrixOperations {
    public static void main(String... args) {
//        AtomicLong v1 = new AtomicLong(0);
//        int[][] a = {{3, 10, 15}, {10, 6, 2}};
//        int[][] at = transpose(a);
//        Arrays.stream(a).forEach(m -> System.out.println(Arrays.toString(m)));
//        System.out.println("******************");
//        Arrays.stream(at).forEach(m -> System.out.println(Arrays.toString(m)));
        int[][] b = {{3, 10, 15}, {10, 6, 2}};
        int[] x = getColumn(b, 1);
        System.out.println(Arrays.toString(x));
//        int[][] bt = sortColumns(b);
//        Arrays.stream(b).forEach(m -> System.out.println(Arrays.toString(m)));
//        System.out.println("******************");
//        Arrays.stream(bt).forEach(m -> System.out.println(Arrays.toString(m)));
//        
//        double[][] btrans = transform(b, x -> 2*x);
//        System.out.println("btrans");
//        Arrays.stream(btrans).forEach(m -> System.out.println(Arrays.toString(m)));
//        System.out.println(Arrays.deepToString(a));
//        System.out.println(Arrays.deepToString(transpose(a)));
//        int[][] b = transpose(a);
//        for (int[] r: b) Arrays.sort(r);
//        int[][] x = transpose(b);
//        System.out.println(Arrays.deepToString(x));
    }
    
    /**
     * getColumn, gets a column from a 2d matrix
     */
    public static int[] getColumn(int[][] matrix, int column) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        if (column < 0 || column >= matrix[0].length)
            throw new IllegalArgumentException("column number incorrect!!");
        return Arrays.stream(matrix).mapToInt(row -> row[column]).toArray();
    }
    
    public static double[] getColumn(double[][] matrix, int column) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        if (column < 0 || column >= matrix[0].length)
            throw new IllegalArgumentException("column number incorrect!!");
        return Arrays.stream(matrix).mapToDouble(row -> row[column]).toArray();
    }
    
    public static long[] getColumn(long[][] matrix, int column) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        if (column < 0 || column >= matrix[0].length)
            throw new IllegalArgumentException("column number incorrect!!");
        return Arrays.stream(matrix).mapToLong(row -> row[column]).toArray();
    }
    
    public static String[] getColumn(String[][] matrix, int column) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        if (column < 0 || column >= matrix[0].length)
            throw new IllegalArgumentException("column number incorrect!!");
        return Arrays.stream(matrix).map(row -> row[column]).toArray(String[]::new);
    }
    
    /**
     * transpose
     */
    public static int[][] transpose(int[][] matrix) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        return IntStream.range(0, matrix[0].length)
                .mapToObj(i -> getColumn(matrix, i))
                .toArray(int[][]::new)
        ;
    }
    
    public static double[][] transpose(double[][] matrix) {
        if (!isRectangular(matrix))
            throw new IllegalArgumentException("matrix is not rectangular!");
        return IntStream.range(0, matrix[0].length)
                .mapToObj(i -> getColumn(matrix, i))
                .toArray(double[][]::new)
        ;
    }
      
    public static long[][] transpose(long[][] matrix) {
        if (!isRectangular(matrix)) {
            throw new IllegalArgumentException("matrix is not rectangular!");
        }
        return IntStream.range(0, matrix[0].length)
                .mapToObj(i -> getColumn(matrix, i))
                .toArray(long[][]::new)
        ;
    }
    
    public static String[][] transpose(String[][] matrix) {
        if (!isRectangular(matrix)) {
            throw new IllegalArgumentException("matrix is not rectangular!");
        }
        return IntStream.range(0, matrix[0].length)
                .mapToObj(i -> getColumn(matrix, i))
                .toArray(String[][]::new)
        ;
    }
    
    public static int[][] sortColumns(int[][] matrix) {
        if (!isRectangular(matrix)) {
            throw new IllegalArgumentException("matrix is not rectangular!");
        }
        int[][] a = transpose(matrix);
        int[][] b = Arrays.stream(a).map(m -> {Arrays.sort(m); return m;}).toArray(int[][]::new);
        return transpose(b);
    }
    
    public static boolean isRectangular(int[][] a) {
        return Arrays.stream(a).mapToInt(array -> array.length).distinct().count() == 1;
    }
    
    public static boolean isRectangular(long[][] a) {
        return Arrays.stream(a).mapToInt(array -> array.length).distinct().count() == 1;
    }
    
    public static boolean isRectangular(double[][] a) {
        return Arrays.stream(a).mapToInt(array -> array.length).distinct().count() == 1;
    }
    
    public static <T> boolean isRectangular(T[][] matrix) {
        return Arrays.stream(matrix).map(array -> array.length).distinct().count() == 1;
    }
    
    public static boolean isSquare(int[][] matrix) {
        return isRectangular(matrix) && matrix.length == matrix[0].length;
    }
    
    public static boolean isSquare(double[][] matrix) {
        return isRectangular(matrix) && matrix.length == matrix[0].length;
    }
   
     public static boolean isSquare(long[][] matrix) {
        return isRectangular(matrix) && matrix.length == matrix[0].length;
    }
    
    public static boolean isSymmetric(int[][] matrix) {
        if (!isSquare(matrix)) return false;
        return IntStream.range(0, matrix.length).allMatch(i -> Arrays.equals(getColumn(matrix, i), matrix[i]));
    }
    
    public static boolean isSymmetric(double[][] matrix) {
        if (!isSquare(matrix)) return false;
        return IntStream.range(0, matrix.length).allMatch(i -> Arrays.equals(getColumn(matrix, i), matrix[i]));
    }

    public static boolean isSymmetric(long[][] matrix) {
        if (!isSquare(matrix)) return false;
        return IntStream.range(0, matrix.length).allMatch(i -> Arrays.equals(getColumn(matrix, i), matrix[i]));
    }
    
    public static double[] transform(double[] d, DoubleUnaryOperator f) {
        return Arrays.stream(d).map(f::applyAsDouble).toArray();
    }

    public static double[][] transform(double[][] d, DoubleUnaryOperator f) {
        return Arrays.stream(d).map(array -> transform(array, f)).toArray(double[][]::new);
    }
}
