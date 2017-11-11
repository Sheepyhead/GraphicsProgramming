package com.sheepy.graphicsprogramming.raytracer;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Sphere> spheres;
    private List<DirectionalLight> directionalLights;
    private List<PointLight> pointLights;
    private float ambientLight;

    public Scene() {
        spheres = new ArrayList<>();
        directionalLights = new ArrayList<>();
        pointLights = new ArrayList<>();
        ambientLight = 0;
    }

    public void addSphere(float radius, Point3D center, Color color, float specular, float reflective) {
        addSphere(new Sphere(radius,center,color,specular, reflective));
    }

    public void addSphere(float radius, Point3D center, Color color, float specular) {
        addSphere(new Sphere(radius,center,color,specular));
    }

    public void addSphere(float radius, Point3D center, Color color) {
        addSphere(new Sphere(radius, center, color));
    }

    public void addSphere(Sphere sphere) {
        spheres.add(sphere);
    }

    public void addDirectionalLight(Point3D direction, float intensity){
        addDirectionalLight(new DirectionalLight(direction, intensity));
    }

    public void addDirectionalLight(DirectionalLight directionalLight) {
        directionalLights.add(directionalLight);
    }

    public void addPointLight(Point3D position, float intensity){
        addPointLight(new PointLight(position,intensity));
    }

    public void addPointLight(PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public List<DirectionalLight> getDirectionalLights() {
        return directionalLights;
    }

    public List<Sphere> getSpheres() {
        return spheres;
    }

    public float getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(float ambientLight) {
        this.ambientLight = ambientLight < 0 ? 0 : ambientLight;
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }
}
