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
    
    public final double[][] x;
    
    public static void main(String... args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class   
           should be named Solution. 
        */

//        Scanner scan = new Scanner(System.in);
//
//        int[] nrs = Arrays.stream(scan.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
//        int obs = nrs[1];
//        int vars = nrs[0];
//
//        double[][] x = new double[obs][vars];
//        double[] y = new double[obs];
//        for (int line = 0; line < obs; line++) {
//            double[] data = Arrays.stream(scan.nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
//            for (int k = 0; k < vars; k++) {
//                x[line][k] = data[k];
//            }
//            y[line] = data[vars];
//        }
//
//        int requested = Integer.parseInt(scan.nextLine());
//        double[][] reqs = new double[requested][2];
//        for (int line = 0; line < requested; line++) {
//            reqs[line] = Arrays.stream(scan.nextLine().split(" "))
//                .mapToDouble(Double::parseDouble)
//                .toArray()
//            ;
//        }
//
//        Matrix M = linearRegression(x, y);
//        for (int i = 0; i < reqs.length; i++) {
//            System.out.format("%.3f", Matrix.dotProduct(M.getColumn(0), addOneToArray(reqs[i])));
//        }
        
        System.out.println("*************    Starting large Matrix inverse ****************************");
        
        long start, end;
        
        for (int i = 2; i <= 40; i++) {
            var m = Matrix.identity(i);
            start = System.currentTimeMillis();
            var det = m.determinant();
            end = System.currentTimeMillis();
            System.out.format("size: %d: determinant: %f  duurde: %.2f seconds%n", i, det, (end - start) / 1000.);
            start = System.currentTimeMillis();
            m.inverse();
            end = System.currentTimeMillis();
            System.out.format("size: %d: %.2f seconds%n", i, (end - start) / 1000.);
            System.out.println("******************************");
        }
        var m = Matrix.of(3, 3, 2, 0, 0, 0, 3, 0, 2, 0, 5);
        var n = m.inverse();
        m.print();
        n.print();
        System.out.println(n.determinant());
        System.out.println("****************************");
        m = Matrix.of(3, 3, 0, 0, 1, 0, 1, 0, 1, 0, 0);
        m.print();
        n = m.inverse();
        n.print();
    }
    
    private static Matrix generate(int size) {
        double[][] large = IntStream.rangeClosed(1, size)
            .mapToObj(i -> IntStream.rangeClosed(1, size).mapToDouble(j -> j).toArray())
            .toArray(double[][]::new)
        ;
        return Matrix.of(large);
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
        double[][] result = IntStream.range(0, rows)
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
        double[][] d = IntStream.range(0, size)
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
    public boolean isRowvector() {
        return x.length == 1;
    }
    
    //-----------------------------------------------------------
    public boolean isColumnvector() {
        return x[0].length == 1;
    }
    
    //-----------------------------------------------------------
    public boolean isVector() {
        return isRowvector() || isColumnvector();
    }
    
    //-----------------------------------------------------------
    public double dotProduct(Matrix m) {
        if (!(this.isVector() && m.isVector())) throw new RuntimeException
            ("Matrices must both be a Vector!!!!");
        if (this.isRowvector()) {
            if (m.isRowvector()) return Matrix.dotProduct(this.getRow(0), m.getRow(0));
            else return Matrix.dotProduct(this.getRow(0), m.getColumn(0));
        }
        else {
            // columnvector
            if (m.isRowvector()) return Matrix.dotProduct(this.getColumn(0), m.getRow(0));
            else return Matrix.dotProduct(this.getColumn(0), m.getColumn(0));
        }
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
        double[][] result = IntStream.range(0, x[0].length)
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
        Matrix m = (Matrix) o;
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
        var copy = copyOfDoubleArray(x);
        int sign = 1;
        for (int i = 0; i < copy.length - 1; i++) {
            if (copy[i][i] == 0) {
                if (!switchRowsSuccesfully(copy, i)) return 0;
                sign = -sign;
            }
            for (int j = i + 1; j < copy.length; j++) {
                wipeRow(copy[i], copy[j], i);
            }
        }
        return sign * IntStream.range(0, copy.length)
            .mapToDouble(i -> copy[i][i])
            .reduce(1, (a, b) -> a * b)
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
        double[][] result = Arrays.stream(x)
            .map(row -> Arrays.stream(row).map(i -> d * i).toArray())
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    public Matrix coFactor() {
        if (!this.isSquare()) throw new RuntimeException("Matrix is not square!!!");
        double[][] result = IntStream.range(0, getRowdimension())
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
        double det = this.determinant();
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
    // public methods
    //***********************************************************************
    
    public Matrix(double[][] x) {
        if (containsNulls(x)) throw new RuntimeException("One or more nulls in the argument!!!");
        if (!isRectangular(x)) throw new RuntimeException("matrix is not rectangular!!!");
        if (!dimensionIsAtLeastOneByOne(x)) throw new RuntimeException("matrixis not a genuine matrix!!!");
        this.x = Arrays.stream(x)
            .map(row -> Arrays.copyOf(row, row.length))
            .toArray(double[][]::new)
        ;
    }

    //-----------------------------------------------------------
    public static boolean containsNulls(double[][] x) {
        return x == null || Arrays.stream(x).anyMatch(row -> row == null);
    }
    
    //-----------------------------------------------------------
    public static boolean isRectangular(double[][] x) {
        return Arrays.stream(x).mapToInt(row -> row.length).distinct().count() == 1;
    }
    
    //-----------------------------------------------------------
    public static boolean dimensionIsAtLeastOneByOne(double[][] x) {
        return x.length >= 1 && x[0].length >= 1;
    }
    
    //-----------------------------------------------------------
    public static double[][] convertToDouble(int[][] x) {
        return Arrays.stream(x)
            .map(row -> Arrays.stream(row).mapToDouble(i -> i).toArray())
            .toArray(double[][]::new)
        ;
    }
    
    //-----------------------------------------------------------
    public static double[][] convertToDouble(long[][] x) {
        return Arrays.stream(x)
            .map(row -> Arrays.stream(row).mapToDouble(i -> i).toArray())
            .toArray(double[][]::new)
        ;
    }
    
    //-----------------------------------------------------------
    public static <S extends Number, U extends Collection<S>, T extends Collection<U>>
                   double[][] convertToDouble(T collection) {
        double[][] result = collection.stream()
            .map(u -> u.stream().mapToDouble(i -> i.doubleValue()).toArray())
            .toArray(double[][]::new)
        ;
        return result;
    }
    
    //-----------------------------------------------------------
    public boolean columnIndexOK(int index) {
        return index >= 0 && index < this.getColumndimension();
    }

    //-----------------------------------------------------------
    public static double[] skipElement(double[] x, int index) {
        if (index < 0 || index > x.length) throw new RuntimeException("index out of bounds");
        return IntStream.range(0, x.length)
            .filter(i -> i != index)
            .mapToDouble(i -> x[i])
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    public static double dotProduct(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Vectors must be of equal length");
        return IntStream.range(0, x.length)
            .mapToDouble(i -> x[i] * y[i])
            .sum()
        ;
    }
    
    //-----------------------------------------------------------
    public static double length(double[] x) {
        return Math.sqrt(dotProduct(x, x));
    }
    
    //-----------------------------------------------------------
    public static boolean matricesAreOkeToAddOrSubtract(Matrix m, Matrix n) {
        return m.getColumndimension() == n.getColumndimension() &&
               m.getRowdimension() == n.getRowdimension()
        ;
    }
    
    //-----------------------------------------------------------
    public static boolean matricesAreOkeToMultiply(Matrix m, Matrix n) {
        return m.getColumndimension() == n.getRowdimension();
    }
    
    //-----------------------------------------------------------
    public static double[] addOneToArray(double[] x) {
        return DoubleStream.concat(DoubleStream.of(1), Arrays.stream(x)).toArray();
    }
    
    //-----------------------------------------------------------
    public static double[][] addOneToArray(double[][] x) {
        return Arrays.stream(x).map(Matrix::addOneToArray).toArray(double[][]::new);
    }
    
    //-----------------------------------------------------------
    public static double[] applyToDoubleArrays(double[] x, double[] y, DoubleBinaryOperator dbo) {
        return IntStream.range(0, x.length)
            .mapToDouble(i -> dbo.applyAsDouble(x[i], y[i]))
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    public static double[] rowOfIdentity(int size, int index) {
        return IntStream.range(0, size)
            .mapToDouble(i -> (i == index ? 1 : 0))
            .toArray()
        ;
    }
    
    //-----------------------------------------------------------
    public static Matrix addOrSubtract(Matrix m, Matrix n, DoubleBinaryOperator dbo) {
        if (!matricesAreOkeToAddOrSubtract(m, n)) throw new RuntimeException
            ("Matrices are of wrong dimensions to add");
        double[][] result = IntStream.range(0, m.getRowdimension())
            .mapToObj(i -> applyToDoubleArrays(m.getRow(i), n.getRow(i), dbo))
            .toArray(double[][]::new)
        ;
        return Matrix.of(result);
    }
    
    //-----------------------------------------------------------
    public static int sign(int i, int j) {
        if (i >= j) return (i - j) % 2 == 0 ? 1 : -1;
        return (j - i) % 2 == 0 ? 1 : -1;
    }
    
    //------------------------------------------------------------
    private static double[][] copyOfDoubleArray(double[][] d) {
        return Arrays.stream(d)
            .map(row -> Arrays.copyOf(row, row.length))
            .toArray(double[][]::new);
    }
    
    //------------------------------------------------------------
    private static void wipeRow(double[] source, double[] target, int index) {
        double factor = target[index] / source[index];
        for (int i = 0; i < source.length; i++) target[i] -= factor * source[i];
    }
    
    //------------------------------------------------------------
    private static boolean switchRowsSuccesfully(double[][] d, int index) {
        if (d[index][index] != 0) return true;
        for (int i = index + 1; i < d.length; i++) {
            if (d[i][index] != 0) {
                var row = d[index];
                d[index] = d[i];
                d[i] = row;
                return true;
            }
        }
        return false;
    }

}
