/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.model.request.user;

import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.validation.ValidEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author idohyeon
 */
@Data
@Builder
public class UpdateUserRequest {
    @Size(min = 2, max = 50)
    @JsonProperty("firstName")
    private String firstName;

    @Size(min = 2, max = 50)
    @JsonProperty("lastName")
    private String lastName;

    @Size(min = 8, max = 20, message = "Username must be between 8 to 20 characters")
    @JsonProperty("username")
    private String username;

    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    @ValidEnum(enumClass=Gender.class)
    @JsonProperty("gender")
    private String gender;

    @Min(value=1, message = "Age must be greater than 0")
    @JsonProperty("age")
    private Integer age;
}
