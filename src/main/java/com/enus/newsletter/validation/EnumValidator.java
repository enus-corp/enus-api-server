/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author idohyeon
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String>{
    private Class<? extends Enum<?>> enumClass;
    private String validValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        validValues = Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value  == null) {
            return true; // handled by @NotNull
        }

        boolean isValid = Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::name)
            .anyMatch(name -> name.equals(value));
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                context.getDefaultConstraintMessageTemplate()
                    .replace("{validValues}", validValues)
            ).addConstraintViolation();
        }

        return isValid;
    }

}
