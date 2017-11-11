package com.sheepy.graphicsprogramming.raytracer;

import javafx.scene.paint.Color;

public class ClampedColor {
    public static Color rgb(int r, int g, int b)
    {
        // Clamp color values
        r = r > 255 ? 255 : r;
        r = r < 0 ? 0 : r;

        g = g > 255 ? 255 : g;
        g = g < 0 ? 0 : g;

        b = b > 255 ? 255 : b;
        b = b < 0 ? 0 : b;

        return Color.rgb(r, g, b);
    }

    public static Color intensifyColor(Color color, float factor) {
        return ClampedColor.rgb((int) (color.getRed() * 255 * factor), (int) (color.getGreen() * 255 * factor), (int) (color.getBlue() * 255 * factor));
    }

    public static Color addColors(Color color1, Color color2) {

        int redValue = (int) ((color1.getRed() + color2.getRed())*255);
        int greenValue = (int) ((color1.getGreen() + color2.getGreen())*255);
        int blueValue = (int) ((color1.getBlue() + color2.getBlue())*255);

        return ClampedColor.rgb(redValue,greenValue,blueValue);
    }
}
