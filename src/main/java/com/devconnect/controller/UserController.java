package com.devconnect.controller;

import com.devconnect.dto.LeetcodeUsernameDTO;
import com.devconnect.dto.UserDTO;
import com.devconnect.entity.User;
import com.devconnect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/me → your own profile
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        UserDTO dto = userService.getMyProfile(currentUser.getEmail());
        return ResponseEntity.ok(dto);
    }

    // GET /api/users/{username} → anyone's public profile
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getPublicProfile(
            @PathVariable String username) {
        UserDTO dto = userService.getPublicProfile(username);
        return ResponseEntity.ok(dto);
    }

// testingg krr rhaa haii apunn !!

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

    // PATCH /api/users/me/leetcode → link leetcode username
    @PatchMapping("/me/leetcode")
    public ResponseEntity<UserDTO> linkLeetcode(
            @AuthenticationPrincipal User currentUser,
            @RequestBody LeetcodeUsernameDTO request) {
        UserDTO dto = userService.linkLeetcode(
                currentUser.getEmail(),
                request.getLeetcodeUsername()
        );
        return ResponseEntity.ok(dto);
    }
}