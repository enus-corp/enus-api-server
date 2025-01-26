package com.enus.newsletter.model.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SignupRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 20, message = "Username must be between 8 to 20 characters")
    private String username;

//    @NotBlank(message = "Gender is required")
//    private int gender;
//
//    private String occupation;
//
//    private List<String> newsCategories;
//
//    private List<String> readingPurpose;
//
//    private List<String> importantFactors;
//
//    private List<String> preferredMediaType;
//
//    @Size(max = 500)
//    private String newSelectionCriteria;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,}$",
            message = "Password must have at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
