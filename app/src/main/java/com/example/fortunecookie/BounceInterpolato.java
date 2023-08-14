package com.example.fortunecookie;

import android.view.animation.Interpolator;

public class BounceInterpolato implements Interpolator {
    private double amplitude = 0.1;
    private double frequency = 10;

    public BounceInterpolato() {}

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / amplitude) *
                Math.cos(frequency * time) + 1);
    }
}