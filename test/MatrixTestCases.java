/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pietsLibrary.Matrix;

/**
 *
 * @author Sylvia
 */
public class MatrixTestCases {
    
    public MatrixTestCases() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    
    //***************************************************************
    // testing Constructors
    //***************************************************************
    @Test(expected = RuntimeException.class)
    public void expectedErrorWhenDoubleIsNotRectangular1() {
        double[][] d = {{1}, {1, 2}};
        Matrix m = Matrix.of(d);
    }
    
    public void expectedErrorWhenDoubleIsNotRectangular2() {
        double[][] d = {{1, 2, 3}, {1, 2, 3, 4}, {1, 2, 3}};
        Matrix m = Matrix.of(d);
    }
    
    @Test(expected = RuntimeException.class)
    public void expectedErrorWhenDoubleIsNotRectangular3() {
        Matrix.of(2, 3, 1d, 2d, 3d, 4d, 5d);
    }
    
    @Test
    public void testConstructor() {
        Matrix d = Matrix.of(2, 3, 1, 2, 3, 4, 5, 6);
        double[][] e = { {1, 2, 3}, {4, 5, 6}};
        assertTrue(d.equals(Matrix.of(e)));
    }
    
    @Test
    public void convertToDoubleWorksFine1() {
        int[][] d = {{1, 2, 3}, {2, 3, 4}};
        double[][] e = {{1, 2, 3}, {2, 3, 4}};
        assertTrue(Matrix.of(d).equals(Matrix.of(e)));
    }
    
    @Test
    public void convertToDoubleWorksFine2() {
        long[][] d = {{1L, 2L, 3L}, {2L, 3L, 4L}};
        double[][] e = {{1, 2, 3}, {2, 3, 4}};
        assertTrue(Matrix.of(d).equals(Matrix.of(e)));
    }
    
    @Test
    public void convertToDoubleWorksFine3() {
        List<List<Integer>> list = List.of(List.of(1, 2, 3), List.of(2, 3, 4));
        double[][] d = {{1, 2, 3}, {2, 3, 4}};
        assertTrue(Matrix.of(list).equals(Matrix.of(d)));
    }

    //****************************************************************
    // testing transpose
    //****************************************************************
    @Test
    public void testTranspose() {
        Matrix m = Matrix.of(new double[][]{{1, 2, 3}, {1, 2, 3}});
        Matrix mT = m.transpose();
        for (int i = 0; i < m.getRowdimension(); i++) {
            assertTrue(Arrays.equals(m.getRow(i), mT.getColumn(i)));
        }
    }
    
    //****************************************************************
    // testing multiply
    //****************************************************************
    @Test(expected = RuntimeException.class)
    public void testExceptionIsThrownWithMultiply() {
        double[][] d = {{1, 2, 3}, {1, 2, 3}};
        double[][] e = { {1, 2}};
        Matrix m = Matrix.of(d), n = Matrix.of(e);
        m.multiply(n); 
    };

    @Test
    public void testMultiply1() {
        double[][] d = {{1, 2}, {3, 4}};
        var m = Matrix.of(d);
        var id = Matrix.identity(2);
        assertTrue(m.multiply(id).equals(m));
        assertTrue(id.multiply(m).equals(m));
    }
    
    @Test
    public void testMultiply2() {
        double[][] d = {{1, 2}, {3, 4}};
        double[][] e = {{1}, {1}};
        double[][] result = {{3}, {7}};
        assertTrue(Matrix.of(d).multiply(Matrix.of(e)).equals(Matrix.of(result)));
    }
    
    @Test
    public void testMultiply3() {
        var m = Matrix.of(3, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        var v = Matrix.of(3, 1, 2, 2, 3);
        assertTrue(m.multiply(v).equals(Matrix.of(3, 1, 15, 36, 57)));
    }
        
    //****************************************************************
    // testing identity matrix
    //****************************************************************
    @Test
    public void testIdentity() {
        assertTrue(Matrix.identity(3).equals(Matrix.of(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}})));
    }
    
    @Test
    public void testIdentity2() {
        var m = Matrix.of(4,4, 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1);
        assertEquals(m, Matrix.identity(4));
    }
    
    @Test
    public void testIdentity3() {
        var m = Matrix.of(4,4, 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,1,1);
        assertFalse(m.equals(Matrix.identity(4)));
    }
    
    @Test(expected = RuntimeException.class)
    public void expectedExceptionWhenAddingTwoMatricesOfDifferentSize() {
        double[][] d = { {1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double[][] e = { {1, 2, 3}, {2, 3}, {3, 4, 5}};
        Matrix.of(d).add(Matrix.of(e));
    }
    
    //****************************************************************
    // testing addition and subtraction
    //****************************************************************
    @Test
    public void testAddition1() {
        Matrix m = Matrix.of(3, 2, 5, 5, 6, 6, 7, 7);
        Matrix n = Matrix.of(3, 2, 2, 0, 3, 0, 0, 1);
        Matrix s = Matrix.of(3, 2, 7, 5, 9, 6, 7, 8);
        assertEquals(m.add(n),s);
        assertEquals(m.add(n), n.add(m));
    }
    
    @Test
    public void testAddition2() {
        Matrix m = Matrix.of(3, 2, .5, .5, .6, .6, .7, .7);
        Matrix n = Matrix.of(new double[][]{{.2, .0}, {.3, .0}, {.0, .1}});
        Matrix s = Matrix.of(3, 2, .5 + .2, .5 + .0, .6 + .3, .6 + .0, .7 + .0, .7 + .1);
        assertEquals(m.add(n), n.add(m));
        assertEquals(n.add(m), s);
        assertEquals(m.add(n), s);
    }

    //****************************************************************
    // testing multiplying with a scalar
    //****************************************************************
    @Test
    public void testMultiplyScalar1() {
        var m = Matrix.of(3, 2, 1, 2, 3, 4, 5, 6);
        var n = m.multiply(2);
        assertTrue(Matrix.of(3, 2, 2, 4, 6, 8, 10, 12).equals(n));
        assertTrue(m.multiply(0).equals(Matrix.of(3, 2, 0, 0, 0, 0, 0, 0)));
        assertTrue(m.multiply(-1).equals(Matrix.of(3, 2, -1, -2, -3, -4, -5, -6)));
    }
    
    //****************************************************************
    // testing cofactor Matrix
    //****************************************************************
    @Test
    public void testCofator1() {
        var m = Matrix.of(2, 2, 1, 3, 2, 4);
        var co = Matrix.of(2, 2, 4, -2, -3, 1);
        assertEquals(m.coFactor(), co);
    }
    
    public void testCofator2() {
        var m = Matrix.of(2, 2, 1, 3, 2, 4);
        var co = Matrix.of(2, 2, 4, -2, -3, 1);
        assertEquals(m.coFactor(), co);
    }
    
    //****************************************************************
    // testing dotproduct
    //****************************************************************
    
    @Test(expected = RuntimeException.class)
    public void expectedRuntimeExceptionWhenOneOfTheArraysIsNotAVector() {
        Matrix m = Matrix.of(3, 2, 1, 2, 3, 4, 5, 6);
        Matrix n = Matrix.of(3, 2, 1, 2, 3, 4, 5, 6);
        double d = m.dotProduct(n);
    }
    
    @Test(expected = RuntimeException.class)
    public void expectedRuntimeExceptionWhenOneOfTheArraysIsNotAVector2() {
        Matrix m = Matrix.of(3, 1, 2, 3);
        Matrix n = Matrix.of(3, 2, 1, 2, 3, 4, 5, 6);
        double d = m.dotProduct(n);
    }
    
    @Test(expected = RuntimeException.class)
    public void expectedRuntimeExceptionWhenVectorsHaveDifferentLenghts() {
        Matrix m = Matrix.of(3, 1, 1, 2, 3);
        Matrix n = Matrix.of(1, 4, 1, 2, 3, 4);
        double d = m.dotProduct(n);
    }
    
    @Test
    public void testRowVectorDotproducRowvector() {
        Matrix m = Matrix.of(3, 1, 1, 2, 3);
        Matrix n = Matrix.of(3, 1, 1, 2, 3);
        assertTrue(m.dotProduct(n) == 14);
    }
  
    @Test
    public void testRowVectorDotproductColumnvector() {
        Matrix m = Matrix.of(3, 1, 1, 2, 3);
        Matrix n = Matrix.of(1, 3, 1, 2, 3);
        assertTrue(m.dotProduct(n) == 14);
        assertEquals(m.multiply(n), Matrix.of(3, 3, 1, 2, 3, 2, 4, 6, 3, 6, 9));
    }

    @Test
    public void testColumnVectorDotproducRowvector() {
        Matrix m = Matrix.of(1, 3, 1, 2, 3);
        Matrix n = Matrix.of(3, 1, 1, 2, 3);
        assertTrue(m.dotProduct(n) == 14);
        assertTrue(m.multiply(n).equals(Matrix.of(new int[][] {{14}})));
    }

    @Test
    public void testColumnVectorDotproducColumnvector() {
        Matrix m = Matrix.of(1, 3, 1, 2, 3);
        Matrix n = Matrix.of(1, 3, 1, 2, 3);
        assertTrue(m.dotProduct(n) == 14);
    }
    
    //****************************************************************
    // testing Determinant
    //****************************************************************
    @Test
    public void testDeterminantOfIdentityIsOne() {
        Matrix m = Matrix.identity(12);
        assertTrue(m.determinant() == 1d);
    }
    
    @Test
    public void determinantChangsSignWhenTwoRowsAreExchanged() {
        Matrix m = Matrix.identity(3);
        Matrix n = Matrix.of(3, 3, 0, 1, 0, 1, 0, 0, 0, 0, 1);
        Matrix o = Matrix.of(3, 3, 0, 1, 0, 0, 0, 1, 1, 0, 0);
        assertEquals(m.determinant(), -n.determinant(), 0d);
        assertEquals(n.determinant(), -o.determinant(), 0d);
    }

}
