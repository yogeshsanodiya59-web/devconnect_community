package com.devconnect.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "developer_profiles")
public class DeveloperProfile {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user ;


//    Gutub stats
    private int githubRepos;
    private int githubCommits;
    private int githubStars;
//    Leetcode stats
    private int leetcodeSolved;
    private int leetcodeEasy;
    private int leetcodeMedium;
    private int leetcodeHard;
//    Final computed score
    private double reputationScore;

    private LocalDateTime lastUpdated = LocalDateTime.now() ;
}
