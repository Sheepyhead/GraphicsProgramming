package com.sheepy.graphicsprogramming.raytracer;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PixelCanvas extends Application {
    private GraphicsContext graphicsContext;

    public static final int screenWidth = 800;
    public static final int screenHeight = 800;

    private Canvas canvas;

    public PixelCanvas() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        Scene scene = new Scene(root, screenWidth, screenHeight);

        canvas = new Canvas(screenWidth, screenHeight);

        graphicsContext = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);



        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void putPixel(int x, int y, Color color)
    {
        graphicsContext.setFill(color);

        graphicsContext.fillRect(xFromCenteredCoordinateSystem(x), yFromCenteredCoordinateSystem(y),1,1);

        graphicsContext.setFill(null);
    }

    /**
     * Converts the x-coordinate of a centered coordinate system to the corresponding x-coordinate of an upper-left cor-
     * nered coordinate system.
     * @param x The x-coordinate of a point in a centered coordinate system
     * @return The corresponding x-coordinate of the point in an upper-left cornered coordinate system
     */
    private int xFromCenteredCoordinateSystem(int x)
    {
        return screenWidth / 2 + x;
    }

    /**
     * Converts the y-coordinate of a centered coordinate system to the corresponding y-coordinate of an upper-left cor-
     * nered coordinate system.
     * @param y The y-coordinate of a point in a centered coordinate system
     * @return The corresponding y-coordinate of the point in an upper-left cornered coordinate system
     */
    private int yFromCenteredCoordinateSystem(int y)
    {
        return screenHeight / 2 - y;
    }

}
