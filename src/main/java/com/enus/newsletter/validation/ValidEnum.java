/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.enus.newsletter.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 * @author idohyeon
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
    EnumValidator.class,
    EnumValidatorString.class, 
    EnumValidatorInt.class} )
public @interface ValidEnum {
    String message() default "Invalid value. Must be one of : {validValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    Class<? extends Enum<?>> enumClass();
}
