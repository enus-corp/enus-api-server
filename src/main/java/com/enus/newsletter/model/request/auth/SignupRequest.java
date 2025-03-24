package com.enus.newsletter.model.request.auth;


import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.validation.ValidEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignupRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    @JsonProperty("firstName")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    @JsonProperty("lastName")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 20, message = "Username must be between 8 to 20 characters")
    @JsonProperty("username")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,}$",
            message = "Password must have at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Gender is required")
    @ValidEnum(enumClass=Gender.class)
    @JsonProperty("gender")
    private String gender;

    @NotNull(message = "Age is required")
    @Min(value=1, message = "Age must be greater than 0")
    @JsonProperty("age")
    private Integer age;
}
