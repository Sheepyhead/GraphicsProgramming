package com.sheepy.graphicsprogramming.raytracer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Sphere {
    private Color color;
    private float radius;
    private Point3D center;
    private float specular;
    private float reflective;



    public Sphere(float radius, Point3D center, Color color, float specular, float reflective) {
        this.radius = radius;
        this.center = center;
        this.color = color;
        this.specular = specular;
        this.reflective = reflective;
    }

    public Sphere(float radius, Point3D center, Color color, float specular) {
        this(radius,center,color,specular,0);
    }


    public Sphere(float radius, Point3D center, Color color) {
        this(radius,center,color,-1,0);
    }

    public Color getColor() {
        return color;
    }

    public float getRadius() {
        return radius;
    }

    public Point3D getCenter() {
        return center;
    }

    public Point3D getNormal(Point3D p) {
        Point3D normalDirection = p.subtract(center);
        return normalDirection.normalize();
    }

    public float getSpecular() {
        return specular;
    }

    public boolean specular() {
        return specular >= 0;
    }

    public float getReflective() {
        return reflective;
    }

    public boolean reflective() {
        return reflective > 0;
    }
}
