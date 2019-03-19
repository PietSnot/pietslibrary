/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Matrix {
    
    private final double[][] x;
    
    public static void main(String... args) {
//        List<String> input = List.of(
//            "2 7", 
//            "0.18 0.89 109.85",
//            "1.0 0.26 155.72",
//            "0.92 0.11 137.66",
//            "0.07 0.37 76.17",
//            "0.85 0.16 139.75",
//            "0.99 0.41 162.6",
//            "0.87 0.47 151.77",
//            "4",
//            "0.49 0.18",
//            "0.57 0.83",
//            "0.56 0.64",
//            "0.76 0.18"
//        );
//        int[] nrs = Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
//        int vars = nrs[0], obs = nrs[1];
//        double[][] x = new double[obs][vars];
//        double[] y = new double[obs];
//        for (int line = 1; line <= obs; line++) {
//            double[] data = Arrays.stream(input.get(line).split(" ")).mapToDouble(Double::parseDouble).toArray();
//            for (int k = 0; k < vars; k++) {
//                x[line - 1][k] = data[k];
//            }
//            y[line - 1] = data[vars];
//        }
//        int requested = Integer.parseInt(input.get(obs + 1));
//        double[][] reqs = new double[requested][2];
//        for (int line = obs + 2; line < input.size(); line++) {
//            reqs[line - obs - 2] = Arrays.stream(input.get(line).split(" "))
//                .mapToDouble(Double::parseDouble)
//                .toArray()
//            ;
//        }
        
        String f = "%10.5f";
        Matrix x = Matrix.of(new double[][]{{5, 6, 7, 8, 9}, {7, 6, 4, 5, 6}});
        Matrix X = Matrix.of(addOneToArray(x.transpose().getData()));
        X.print(f);
        System.out.println("*********************");
        Matrix XT = X.transpose();
        XT.print(f);
        System.out.println("*********************");
        Matrix Y = Matrix.of(5, 1, 10, 20, 60, 40, 50);
        Y.print(f);
        System.out.println("*********************");
        Matrix XTX = XT.multiply(X);
        XTX.print(f);
        System.out.println("*********************");
        Matrix XTXmin1 = XTX.inverse();
        XTXmin1.print(f);
        System.out.println("*********************");
        Matrix solution = XT.multiply(X).inverse().multiply(XT).multiply(Y);
        solution.print(f);
        System.out.println("*********************");
//        Matrix t = Matrix.of(new double[][] {{1, 5, 5}}).multiply(solution);
//        t.print(f);
        System.out.println("*********************");
        System.out.println("*********************");
        Matrix xx = Matrix.of(5, 2, 5, 7, 6, 6, 7, 4, 8, 5, 9, 6);
        Matrix xxx = Matrix.of(addOneToArray(xx.getData()));
        Matrix xxxT = xxx.transpose();
        Matrix obsY = Matrix.of(5, 1, 10, 20, 60, 40, 50);
        Matrix hh = xxxT.multiply(xxx).inverse().multiply(xxxT).multiply(obsY);
        hh.print(f);
        System.out.println("*********************");
        System.out.println("*********************");
        double[][] oX = {{5, 7}, {6, 6}, {7, 4}, {8, 5}, {9, 6}};
        double[] oY = {10, 20, 60, 40, 50};
        Matrix flup = linearRegression(oX, oY);
    }
    
    
    //********************************************************************
    // Constructors
    //********************************************************************

    public static Matrix of(double[][] x) {
        return new Matrix(x);
    }
    
    //-----------------------------------------------------------
    public static Matrix of(int[][] x) {
        return Matrix.of(convertToDouble(x));
    }
    
    //-----------------------------------------------------------
    public static Matrix of(long[][] x) {
        return Matrix.of(convertToDouble(x));
    }
    
    //-----------------------------------------------------------
    public static 
        <S extends Number, U extends Collection<S>, T extends Collection<U>>
        Matrix of(T x) {
        return Matrix.of(convertToDouble(x));
    }
        
    //-----------------------------------------------------------
    public static Matrix of(double[] x) {
        return Matrix.of(new double[][]{x});     
    }
    
    //-----------------------------------------------------------
    public static Matrix of(int[] x) {
        return Matrix.of(new int[][]{x});
    }
    
    //-----------------------------------------------------------
    public static Matrix of(long[] x) {
        return Matrix.of(new long[][] {x});
    }
    
    //-----------------------------------------------------------
    public static Matrix of(List<? extends Number> list) {
        return Matrix.of(list.stream().mapToDouble(i -> i.doubleValue()).toArray());
    }
        
    //-----------------------------------------------------------
    public static Matrix of(int rows, int columns, double... doubles) {
        if (rows * columns != doubles.length) throw new RuntimeException
            ("rows and columns don't match number of doubles!!!");
        var result = IntStream.range(0, rows)
            .mapToObj(i -> IntStream.range(0, columns)
                               .mapToDouble(j -> doubles[i * columns + j])
                               .toArray())
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //********************************************************************
    // public (static) methods
    //********************************************************************

    public static Matrix identity(int size) {
        var d = IntStream.range(0, size)
            .mapToObj(i -> rowOfIdentity(size, i))
            .toArray(double[][]::new)
        ;
        return Matrix.of(d);
    }
    
    //-----------------------------------------------------------
    public int getRowdimension() {
        return x.length;
    }
    
    //-----------------------------------------------------------
    public int getColumndimension() {
        return x[0].length;
    }
        
    //-----------------------------------------------------------
    public double[] getRow(int row) {
        if (row < 0 || row >= getRowdimension()) throw new RuntimeException("row index out of bound!");
        return Arrays.copyOf(x[row], x[row].length);
    }
    
    //-----------------------------------------------------------
    public double[] getColumn(int column) {
        if (column < 0 || column >= getColumndimension()) throw new RuntimeException(
            "Wrong column index");
        return Arrays.stream(x).mapToDouble(row -> row[column]).toArray();
    }
    
    //-----------------------------------------------------------
    public boolean isSquare() {
        return x.length == x[0].length;
    }
    
    //-----------------------------------------------------------
    public double[][] getData() {
        return Arrays.stream(x)
            .map(row -> Arrays.copyOf(row, row.length))
            .toArray(double[][]::new)
        ;
    }
    
    //-----------------------------------------------------------
    public Matrix transpose() {
        var result = IntStream.range(0, x[0].length)
            .mapToObj(this::getColumn)
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Matrix)) return false;
        var m = (Matrix) o;
        return IntStream.range(0, x.length)
            .allMatch(i -> Arrays.equals(x[i], m.getRow(i)))
        ;
    }

    //-----------------------------------------------------------
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Arrays.deepHashCode(this.x);
        return hash;
    }
    
    //-----------------------------------------------------------
    public boolean isSymmetric() {
        return equals(transpose());
    }
    
    //-----------------------------------------------------------
    public Matrix minorMatrix(int skipRow, int skipColumn) {
        double[][] result = IntStream.range(0, x.length)
            .filter(i -> i != skipRow)
            .mapToObj(i -> skipElement(x[i], skipColumn))
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    public double minor(int skipRow, int skipColumn) {
        return minorMatrix(skipRow, skipColumn).determinant();
    }
    
    //-----------------------------------------------------------
    public double determinant() {
        if (!isSquare()) throw new RuntimeException("Matrix must be square!!!");
        if (x.length == 1) return x[0][0];
        return IntStream.range(0, x.length)
            .mapToDouble(i -> x[i][0] * (i % 2 == 0 ? 1 : -1) * minor(i, 0))
            .sum()
        ;
    }
    
    //-----------------------------------------------------------
    public Matrix multiply(Matrix m) {
        if (this.getColumndimension() != m.getRowdimension())
            throw new RuntimeException("Matrices have incorrect dimensions to multiply!");
        double[][] y = m.transpose().getData();
        double[][] result = Arrays.stream(x)
            .map(xrow -> Arrays.stream(y).mapToDouble(yrow -> dotProduct(xrow, yrow)).toArray())
            .toArray(double[][]::new)
        ;
        return new Matrix(result);
    }
    
    //-----------------------------------------------------------
    public Matrix add(Matrix m) {
        return addOrSubtract(this, m, (a, b) -> a + b);
    }
    
    //-----------------------------------------------------------
    public Matrix subtract(Matrix m) {
        return addOrSubtract(this, m, (a, b) -> a - b);
    }
    
    //-----------------------------------------------------------
    public Matrix multiply(double d) {
        var result = Arrays.stream(x)
            .map(row -> Arrays.stream(row).map(i -> d * i).toArray())
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    public Matrix coFactor() {
        if (!this.isSquare()) throw new RuntimeException("Matrix is not square!!!");
        var result = IntStream.range(0, getRowdimension())
            .mapToObj(i -> IntStream.range(0, getColumndimension())
                .mapToDouble(j -> this.minor(i, j) * sign(i, j))
                .toArray()
            )
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    public Matrix inverse() {
        var det = this.determinant();
        if (Math.abs(det) < .01) throw new RuntimeException
            ("determinant is zero or too low ( < .01) to calculate inverse!!!!!");
        return this.coFactor().transpose().multiply(1 / det);
    }
    
    //-----------------------------------------------------------
    public static Matrix linearRegression(double[][] X, double[] Y) {
        Matrix m = Matrix.of(addOneToArray(X));
        Matrix mT = m.transpose();
        Matrix result = mT.multiply(m).inverse().multiply(mT).multiply(Matrix.of(Y).transpose());
        return result;
    }
    
    //-----------------------------------------------------------
    public void print(String format) {
        Arrays.stream(x).forEach( row -> {
                Arrays.stream(row).forEach(d -> System.out.format(format, d));
                System.out.println();
            }
        );
    }
    
    public void print() {
        print("%8.2f");
    }
        
    //***********************************************************************
    // private methods
    //***********************************************************************
    
    private Matrix(double[][] x) {
        if (containsNulls(x)) throw new RuntimeException("One or more nulls in the argument!!!");
        if (!isRectangular(x)) throw new RuntimeException("matrix is not rectangular!!!");
        if (!dimensionIsAtLeastOneByOne(x)) throw new RuntimeException("matrixis not a genuine matrix!!!");
        this.x = Arrays.stream(x)
            .map(row -> Arrays.copyOf(row, row.length))
            .toArray(double[][]::new)
        ;
    }

    //-----------------------------------------------------------
    private static boolean containsNulls(double[][] x) {
        return x == null || Arrays.stream(x).anyMatch(row -> row == null);
    }
    
    //-----------------------------------------------------------
    private static boolean isRectangular(double[][] x) {
        return Arrays.stream(x).mapToInt(row -> row.length).distinct().count() == 1;
    }
    
    //-----------------------------------------------------------
    private static boolean dimensionIsAtLeastOneByOne(double[][] x) {
        return x.length >= 1 && x[0].length >= 1;
    }
    
    //-----------------------------------------------------------
    private static double[][] convertToDouble(int[][] x) {
        return Arrays.stream(x)
            .map(row -> Arrays.stream(row).mapToDouble(i -> i).toArray())
            .toArray(double[][]::new)
        ;
    }
    
    //-----------------------------------------------------------
    private static double[][] convertToDouble(long[][] x) {
        return Arrays.stream(x)
            .map(row -> Arrays.stream(row).mapToDouble(i -> i).toArray())
            .toArray(double[][]::new)
        ;
    }
    
    //-----------------------------------------------------------
    private static <S extends Number, U extends Collection<S>, T extends Collection<U>>
                   double[][] convertToDouble(T collection) {
        double[][] result = collection.stream()
            .map(u -> u.stream().mapToDouble(i -> i.doubleValue()).toArray())
            .toArray(double[][]::new)
        ;
        return result;
    }
    
    //-----------------------------------------------------------
    private boolean columnIndexOK(int index) {
        return index >= 0 && index < this.getColumndimension();
    }

    //-----------------------------------------------------------
    private static double[] skipElement(double[] x, int index) {
        if (index < 0 || index > x.length) throw new RuntimeException("index out of bounds");
        return IntStream.range(0, x.length)
            .filter(i -> i != index)
            .mapToDouble(i -> x[i])
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    private static double dotProduct(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Vectors must be of equal length");
        return IntStream.range(0, x.length)
            .mapToDouble(i -> x[i] * y[i])
            .sum()
        ;
    }
    
    //-----------------------------------------------------------
    private static double length(double[] x) {
        return Math.sqrt(dotProduct(x, x));
    }
    
    //-----------------------------------------------------------
    private static boolean matricesAreOkeToAddOrSubtract(Matrix m, Matrix n) {
        return m.getColumndimension() == n.getColumndimension() &&
               m.getRowdimension() == n.getRowdimension()
        ;
    }
    
    //-----------------------------------------------------------
    private static boolean matricesAreOkeToMultiply(Matrix m, Matrix n) {
        return m.getColumndimension() == n.getRowdimension();
    }
    
    //-----------------------------------------------------------
    private static double[] addOneToArray(double[] x) {
        return DoubleStream.concat(DoubleStream.of(1), Arrays.stream(x)).toArray();
    }
    
    //-----------------------------------------------------------
    private static double[][] addOneToArray(double[][] x) {
        return Arrays.stream(x).map(Matrix::addOneToArray).toArray(double[][]::new);
    }
    
    //-----------------------------------------------------------
    private static double[] applyToDoubleArrays(double[] x, double[] y, DoubleBinaryOperator dbo) {
        return IntStream.range(0, x.length)
            .mapToDouble(i -> dbo.applyAsDouble(x[i], y[i]))
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    private static double[] rowOfIdentity(int size, int index) {
        return IntStream.range(0, size)
            .mapToDouble(i -> (i == index ? 1 : 0))
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    private static Matrix addOrSubtract(Matrix m, Matrix n, DoubleBinaryOperator dbo) {
        if (!matricesAreOkeToAddOrSubtract(m, n)) throw new RuntimeException
            ("Matrices are of wrong dimensions to add");
        double[][] result = IntStream.range(0, m.getRowdimension())
            .mapToObj(i -> applyToDoubleArrays(m.getRow(i), n.getRow(i), dbo))
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    private static int sign(int i, int j) {
        if (i >= j) return (i - j) % 2 == 0 ? 1 : -1;
        return (j - i) % 2 == 0 ? 1 : -1;
    }
}
