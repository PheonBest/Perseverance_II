package app.utils;

public class MathUtils {

	  private final static double PI = Math.PI;
	  private final static double TWO_PI = 2 * PI;
	  
	  /**
	   * Normalizes an angle to a relative angle.
	   * The normalized angle will be in the range from -PI to PI, where PI
	   * itself is not included.
	   *
	   * Source: http://www.java2s.com/Tutorial/Java/0120__Development/Normalizesanangletoarelativeangle.htm
	   *
	   * @param angle the angle to normalize
	   * @return the normalized angle that will be in the range of [-PI,PI[
	   */
	  public static double normalRelativeAngle(double angle) {
	    return (angle %= TWO_PI) >= 0 ? (angle < PI) ? angle : angle - TWO_PI : (angle >= -PI) ? angle : angle + TWO_PI;
	  }
	  
	  /**
	   * Normalizes an angle to a relative angle.
	   * The normalized angle will be in the range from -180 to 180, where 180
	   * itself is not included.
	   *
	   * Source: http://www.java2s.com/Tutorial/Java/0120__Development/Normalizesanangletoarelativeangle.htm
	   * (modified version, changed PI/TWO_PI to 180/360)
	   *
	   * @param angle the angle to normalize
	   * @return the normalized angle that will be in the range of [-180,180[
	   */
	  public static double normalRelativeAngleDeg(double angle) {
		    return (angle %= 360) >= 0 ? (angle < 180) ? angle : angle - 360 : (angle >= -180) ? angle : angle + 360;
      }
	  
	  /**
	   * Normalizes an angle to an absolute angle.
	   * The normalized angle will be in the range from 0 to 360, where 360
	   * itself is not included.
	   *
	   * Source: http://www.java2s.com/Tutorial/Java/0120__Development/Normalizesanangletoanabsoluteangle.htm
	   *
	   * @param angle the angle to normalize
	   * @return the normalized angle that will be in the range of [0,360[
	   */
	  public static double normalAbsoluteAngleDegrees(double angle) {
	    return (angle %= 360) >= 0 ? angle : (angle + 360);
	  }
	  
		public static double normalise(double value, double start, double end) {

			double width = end - start; //
			double offsetValue = value - start; // value relative to 0

			return (offsetValue - (Math.floor(offsetValue / width) * width)) + start;
			// + start to reset back to start of original range
		}
}
