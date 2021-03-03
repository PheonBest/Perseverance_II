package app.utils;

public class Rotate {
    public static double[] rotatePointAroundCenter(double radianAngle, double xCoord, double yCoord, double xRotationPoint, double yRotationPoint) {
        xCoord -= xRotationPoint;
        yCoord -= yRotationPoint;

        double newX = xCoord * Math.cos(radianAngle) - yCoord * Math.sin(radianAngle);
        double newY = xCoord * Math.sin(radianAngle) + yCoord * Math.cos(radianAngle);

        return new double[] { newX + xRotationPoint, newY + yRotationPoint };
    }
}