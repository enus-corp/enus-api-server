/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.enus.newsletter.enums;

/**
 *
 * @author idohyeon
 */
public enum Gender {
    male,
    female,
    other
    ;

    public static Gender fromString(String gender) {
        if (gender == null) {
            return Gender.other;
        }
        return switch (gender) {
            case "M" -> Gender.male;
            case "F" -> Gender.female;
            default -> Gender.other;
        };
    }
}
