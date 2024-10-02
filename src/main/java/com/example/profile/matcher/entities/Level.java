package com.example.profile.matcher.entities;

public class Level {

    private int min;
    private int max;


    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Level{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
