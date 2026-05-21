package com.devconnect.controller;

import com.devconnect.dto.LeetcodeUsernameDTO;
import com.devconnect.dto.UserDTO;
import com.devconnect.entity.User;
import com.devconnect.service.ScoreService;
import com.devconnect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ScoreService scoreService;

    public UserController(UserService userService,
                          ScoreService scoreService) {
        this.userService = userService;
        this.scoreService = scoreService;
    }

    // Your own full profile
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                userService.getMyProfile(currentUser.getEmail())
        );
    }

    // Anyone's public profile by github username
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getPublicProfile(
            @PathVariable String username) {
        return ResponseEntity.ok(
                userService.getPublicProfile(username)
        );
    }

    // Link your LeetCode account
    @PatchMapping("/me/leetcode")
    public ResponseEntity<UserDTO> linkLeetcode(
            @AuthenticationPrincipal User currentUser,
            @RequestBody LeetcodeUsernameDTO request) {
        return ResponseEntity.ok(
                userService.linkLeetcode(
                        currentUser.getEmail(),
                        request.getLeetcodeUsername()
                )
        );
    }

    // Refresh your reputation score manually
    @PostMapping("/me/refresh-score")
    public ResponseEntity<UserDTO> refreshScore(
            @AuthenticationPrincipal User currentUser) {
        scoreService.refreshScore(currentUser);
        return ResponseEntity.ok(
                userService.getMyProfile(currentUser.getEmail())
        );
    }
}