package com.devconnect.dto;


import lombok.Data;

@Data
public class UserDTO{
    private Long id;
    private String name;
    private String email;
    private String avatarUrl;
    private String githubUsername;
    private String leetcodeUsername;
    private String role;
    private double reputationScore;
    private int githubRepos;
    private int githubCommits;
    private int githubStars;
    private int leetcodeSolved;
    private int leetcodeEasy;
    private int leetcodeMedium;
    private int leetcodeHard;
}
