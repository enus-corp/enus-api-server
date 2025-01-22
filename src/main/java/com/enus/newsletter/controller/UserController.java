package com.enus.newsletter.controller;

import com.enus.newsletter.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="User", description = "User API")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/:id")
    public ResponseEntity<String> getUser(@RequestParam("id") Integer id) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/updateUser")
    public void updateUser() {}

    @DeleteMapping("/delete")
    public void delete() {}

    @PostMapping("/changePassword")
    public void changePassword(){}
}
