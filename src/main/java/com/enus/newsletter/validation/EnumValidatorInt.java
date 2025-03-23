/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idohyeon
 */
@Slf4j
public class EnumValidatorInt implements ConstraintValidator<ValidEnum, Integer>{
    private Class<? extends Enum<?>> enumClass;
    private String validValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        
        // Create a string showing valid ordinal values and their corresponding enum names
        validValues = Arrays.stream(enumClass.getEnumConstants())
            .map(constant -> constant.name() + "(" + ((Enum<?>) constant).ordinal() + ")")
            .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // handled by @NotNull
        }

        // Check if the integer value matches any enum ordinal value
        Enum<?>[] constants = enumClass.getEnumConstants();
        log.info("Enum class: {}", enumClass);
        log.info("Enum constants: {}", constants);
        log.info("Enum value: {}", value);
        log.info("Valid values: {}", validValues);

        boolean isValid = value >= 0 && value < constants.length;
        
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
