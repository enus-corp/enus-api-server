package com.enus.newsletter.controller;

import com.enus.newsletter.interfaces.CustomUserDetailsImpl;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.service.UserService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "UserController")
@Tag(name="User", description = "User API")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/self")
    public ResponseEntity<GeneralServerResponse<UserDTO>> self(@AuthenticationPrincipal CustomUserDetailsImpl currentUser) {
        log.info("[UserController][Self] Fetching user info");
        log.info("[UserController][Self] User ID: {}", currentUser.getEmail());
        UserDTO user = userService.getUser(currentUser.getEmail());
        return ResponseEntity.ok(new GeneralServerResponse<>(false, null, 0, user));
    }
    @PostMapping("/updateUser")
    public void updateUser() {}

    @DeleteMapping("/delete")
    public void delete() {}

    @PostMapping("/changePassword")
    public void changePassword(){}
}
