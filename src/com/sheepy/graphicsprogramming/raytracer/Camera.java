package com.sheepy.graphicsprogramming.raytracer;

import com.sun.javafx.geom.Matrix3f;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;

public class Camera extends Application {
    private PixelCanvas canvas;
    private Point3D position;
    private Matrix3f rotation;
    private ViewPort viewPort;
    private Scene scene;
    private final static Color BACKGROUND_COLOR = Color.BLACK;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        position = new Point3D(3, 0, 1);
        rotation = new Matrix3f(
                0.7071f,0,-0.7071f,
                0,1,0,
                0.7071f,0,0.7071f);

        canvas = new PixelCanvas();
        viewPort = new ViewPort();
        scene = new Scene();

        scene.addSphere(1, new Point3D(0, -1, 3), Color.RED, 500, 0.2f);
        scene.addSphere(1, new Point3D(2, 0, 4), Color.BLUE, 500, 0.3f);
        scene.addSphere(1, new Point3D(-2, 0, 4), Color.GREEN, 10,0.4f);
        scene.addSphere(5000, new Point3D(0, -5001, 0), Color.YELLOW, 1000,0.5f);

        scene.setAmbientLight(0.2f);

        scene.addDirectionalLight(new Point3D(0, 2, -1), 0.6f);
        scene.addDirectionalLight(new Point3D(1, 4, 4), 0.2f);


        canvas.start(primaryStage);

        renderScene();
    }

    private void renderScene() {
        for (int x = -PixelCanvas.screenWidth / 2; x <= PixelCanvas.screenWidth / 2; x++) {
            for (int y = -PixelCanvas.screenHeight / 2; y <= PixelCanvas.screenHeight / 2; y++) {
                Point3D direction = matrixVectorProduct(rotation,viewPort.canvasToViewport(new Point(x, y)));
                Color color = traceRay(position, direction, 5, 1);
                canvas.putPixel(x, y, color);
            }
        }
    }

    private Color traceRay(Point3D origin, Point3D direction, int recursionDepth, float minDistance) {
        return traceRay(origin, direction, recursionDepth, minDistance, Float.MAX_VALUE);
    }

    private Color traceRay(Point3D origin, Point3D direction, int recursionDepth, float minDistance, float maxDistance) {
        Pair<Sphere, Float> resultPair = closestIntersection(origin, direction, minDistance, maxDistance);

        Sphere closestSphere = resultPair.getKey();
        float closestHit = resultPair.getValue();

        if (closestSphere == null) {
            return BACKGROUND_COLOR;
        }

        Point3D p = origin.add(direction.multiply(closestHit));
        Point3D n = p.subtract(closestSphere.getCenter());
        n = n.multiply(1f / n.magnitude());

        float lightIntensity = computeLighting(p, n, direction.multiply(-1f), closestSphere);
        Color sphereColor = ClampedColor.intensifyColor(closestSphere.getColor(), lightIntensity);

        // Recursion limit or non-reflective surface reached
        if (recursionDepth <= 0 || !closestSphere.reflective())
        {
            return sphereColor;
        }

        // Recurse again
        Point3D r = reflectRay(direction.multiply(-1), n);
        Color reflectedColor = traceRay(p,r,recursionDepth - 1,0.001f);

        return ClampedColor.addColors(
                ClampedColor.intensifyColor(sphereColor,1-closestSphere.getReflective()),
                ClampedColor.intensifyColor(reflectedColor,closestSphere.getReflective()));
    }

    private float[] intersectRaySphere(Point3D origin, Point3D direction, Sphere sphere) {
        float[] result = new float[2];
        Point3D c = sphere.getCenter();
        float r = sphere.getRadius();
        Point3D oc = origin.subtract(c);

        double k1 = direction.dotProduct(direction);
        double k2 = 2d * oc.dotProduct(direction);
        double k3 = oc.dotProduct(oc) - r * r;

        double discriminant = k2 * k2 - 4d * k1 * k3;
        if (discriminant < 0) {
            result[0] = Float.MAX_VALUE;
            result[1] = Float.MAX_VALUE;
        } else {
            result[0] = (float) ((-k2 + Math.sqrt(discriminant)) / (2d * k1));
            result[1] = (float) ((-k2 - Math.sqrt(discriminant)) / (2d * k1));
        }

        return result;
    }

    private float computeLighting(Point3D p, Point3D n, Point3D v, Sphere sphere) {
        float intensity = scene.getAmbientLight();

        Point3D l;
        for (PointLight light : scene.getPointLights()) {
            float maxDistance = 1f;
            l = light.getPosition().subtract(p);

            // Shadow check
            Pair<Sphere, Float> temp = closestIntersection(p, l, 0.001f, maxDistance);
            if (temp.getKey() != null) continue;

            // Diffuse
            double nDotL = n.dotProduct(l);
            if (nDotL > 0) {
                double addIntensity = light.getIntensity() * nDotL / (n.magnitude() * l.magnitude());
                intensity += addIntensity;
            }

            // Specular
            if (sphere.specular()) {
                Point3D r = reflectRay(l,n);
                double rDotV = r.dotProduct(v);
                if (rDotV > 0) {
                    double addIntensity = light.getIntensity() * Math.pow(rDotV / (r.magnitude() * v.magnitude()), sphere.getSpecular());
                    intensity += addIntensity;
                }
            }
        }
        for (DirectionalLight light : scene.getDirectionalLights()) {
            l = light.getDirection();
            float maxDistance = Float.MAX_VALUE;

            // Shadow check
            Pair<Sphere, Float> temp = closestIntersection(p, l, 0.001f, maxDistance);
            if (temp.getKey() != null) continue;

            // Diffuse
            double nDotL = n.dotProduct(l);

            if (nDotL > 0) {
                double addIntensity = light.getIntensity() * nDotL / (n.magnitude() * l.magnitude());
                intensity += addIntensity;
            }

            // Specular
            if (sphere.specular()) {
                Point3D r = reflectRay(l,n);
                double rDotV = r.dotProduct(v);
                if (rDotV > 0) {
                    double addIntensity = light.getIntensity() * Math.pow(rDotV / (r.magnitude() * v.magnitude()), sphere.getSpecular());
                    intensity += addIntensity;
                }
            }
        }
        return intensity;
    }

    private Pair<Sphere, Float> closestIntersection(Point3D origin, Point3D direction, float minDistance, float maxDistance) {
        float closestHit = Float.MAX_VALUE;
        Sphere closestSphere = null;

        for (Sphere sphere : scene.getSpheres()) {
            float[] intersections = intersectRaySphere(origin, direction, sphere);
            if ((intersections[0] > minDistance && intersections[0] < maxDistance) && intersections[0] < closestHit) {
                closestHit = intersections[0];
                closestSphere = sphere;
            }
            if ((intersections[1] > minDistance && intersections[1] < maxDistance) && intersections[1] < closestHit) {
                closestHit = intersections[1];
                closestSphere = sphere;
            }
        }

        return new Pair<>(closestSphere, closestHit);
    }

    private Point3D reflectRay(Point3D r, Point3D n) {
        return n.multiply(n.dotProduct(r) *  2d).subtract(r);
    }

    private Point3D matrixVectorProduct(Matrix3f m, Point3D v) {
        double x = m.m00*v.getX()+m.m01*v.getY()+m.m02*v.getZ();
        double y = m.m10*v.getX()+m.m11*v.getY()+m.m12*v.getZ();
        double z = m.m20*v.getX()+m.m21*v.getY()+m.m22*v.getZ();

        return new Point3D(x, y, z);

    }
}
