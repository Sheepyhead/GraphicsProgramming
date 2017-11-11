package com.sheepy.graphicsprogramming.raytracer;


import javafx.geometry.Point3D;

public class DirectionalLight {

    private Point3D direction;
    private float intensity;

    public DirectionalLight(Point3D direction, float intensity) {
        this.direction = direction;
        this.intensity = intensity;
    }

    public Point3D getDirection() {
        return direction;
    }

    public float getIntensity() {
        return intensity;
    }
}
