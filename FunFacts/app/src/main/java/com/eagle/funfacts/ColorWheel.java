package com.eagle.funfacts;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by 1 on 16.07.2015.
 */
public class ColorWheel {
    private String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7" // light gray
    };

    public int getColor() {

        Random randomManager = new Random();
        int randomNumber = randomManager.nextInt(mColors.length);

        String color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
