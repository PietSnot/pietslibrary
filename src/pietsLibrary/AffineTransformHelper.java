package pietsLibrary;

import java.awt.geom.AffineTransform;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Piet
 */
public class AffineTransformHelper {
   public static void main(String... args) {
      int width = 500, height = 500;
      AffineTransform at = createTransform(-1, 1, 1, 2, 0, 0, width - 1, height - 1, false);
      System.out.println(at);
      System.out.println(pixelsize(at));
   }
   
    /**
    * this method creates an AffineTransform that transforms coordinates
    * given in left, righ, top and bottom coordinate system to that
    * of the supplied original coordinates.
    * 
    * Usage: say you have a panel with top left (0,0) and bottom right 
    * (width, height), and the coordinate system you want to use is
    * topleft (-5, 6) - bottom right (100, -50), then use
    * createTransform(-5, 6, 100, -50, 0, 0, width, height, true/false)
    * 
    * @param left coordinate of the leftmost point of the user coordinates
    * @param top upmost coordinate of user coordinates
    * @param right coordinate of the rightmost point of the user coordinates
    * @param bottom coordinate of the bottom of the user coordinates
    * @param originalLeft left coordinate of original coordinate system
    * @param originalTop upper coordinate of original coordinate system
    * @param originalRight right coordinate of original coordinate system
    * @param originalBottom bottom coordinate of original coordinate system
    * @param keepAspectratio if true, the aspectratio of the original coord. system is retained
    * @return an AffineTransform to be applied to a graphics environment
    */
   public static AffineTransform createTransform(
           double left, double top, double right, double bottom,
           double originalLeft, double originalTop, 
           double originalRight, double originalBottom,
           boolean keepAspectratio
         ) {
      if (keepAspectratio) {
          double alpha =      (   Math.abs(originalRight - originalLeft) * Math.abs(top - bottom) -
                                  Math.abs(originalTop - originalBottom) * (Math.abs(right - left))
                              ) 
                          /
                              (Math.abs(originalTop - originalBottom) * 2);
          left -= alpha;
          right += alpha;
      }    
      double a, b, c, d, e, f;
      a = (originalRight - originalLeft) / (right - left);
      b = 0;
      c = 0;
      d = (originalBottom - originalTop) / (bottom - top);
      e = originalLeft - a * left;
      f = originalBottom - d * bottom;
      return new AffineTransform(a, c, b, d, e, f);
   }
   
   /**
    * given an AffineTransform at, calculates another AffineTransform to be used
    * in deriving a font tha is suitable for the scaling in at.
    * Say, we have an at, and a font f. Now, the usage is:
    * AffineTrnansform atfont = AffineTransform.createFontTransform(at);
    * fontToUse = f.deriveFont(atfont);
    * and use fontToUse in your drawing routines
    * @return see above
    */
   public static AffineTransform createFontTransform(AffineTransform at) {
       double scaleX = at.getScaleX(), scaleY = at.getScaleY();
       return AffineTransform.getScaleInstance(1 / scaleX, 1 / scaleY);
   }
   
   /**
    * gives the pixelsize of a unit after AffineTransform 'at' is applied
    * @param at the AffineTransform in question
    * @return doube number of pixels of one unit in 'at' coordinates
    */
   public static double pixelsize(AffineTransform at) {
      double scalex = Math.abs(at.getScaleX());
      double scaley = Math.abs(at.getScaleY());
      if (scalex != 0.0) return 1 / scalex;
      else if (scaley != 0.0) return 1 / scaley;
      else return 0;
   }
}

