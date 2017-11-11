package com.sheepy.graphicsprogramming.raytracer;

import javafx.geometry.Point3D;

import java.awt.*;

public class ViewPort {
    private float height;
    private float width;
    private float distance;
    private final static float sizeConstant = 1;

    public ViewPort() {
        height = sizeConstant;
        width = sizeConstant;
        distance = sizeConstant;
    }

    public Point3D canvasToViewport(Point point)
    {
        return new Point3D(point.getX()*(width/PixelCanvas.screenWidth), point.getY()*(height/PixelCanvas.screenHeight), distance);
    }
}
