package com.sheepy.graphicsprogramming.raytracer;

import javafx.geometry.Point3D;

public class PointLight {
    private Point3D position;
    private float intensity;

    public PointLight(Point3D position, float intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    public Point3D getPosition() {
        return position;
    }

    public float getIntensity() {
        return intensity;
    }
}
