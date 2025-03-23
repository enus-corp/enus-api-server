/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.enus.newsletter.enums;

/**
 *
 * @author idohyeon
 */
public enum DetailLevel {
    simple(150),
    basic(300),
    detailed(450),
    extensive(600);

    private final int value;

    DetailLevel(int value)  {
        this.value = value;
    }
}
