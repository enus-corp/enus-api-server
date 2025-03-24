/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.enus.newsletter.enums;

/**
 *
 * @author idohyeon
 */
public enum ReadSpeed {
    slow(0.8),
    normal(1.0),
    fast(1.2),
    veryFast(1.5);

    private final double value;

    ReadSpeed(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static double getValueByName(String name) {
        return ReadSpeed.valueOf(name.toLowerCase()).getValue();
    }
}
