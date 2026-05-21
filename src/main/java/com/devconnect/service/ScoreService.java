package com.devconnect.service;

import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.User;
import com.devconnect.repository.DeveloperProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class ScoreService {

    private final GitHubService gitHubService;
    private final LeetCodeService leetCodeService;
    private final DeveloperProfileRepository developerProfileRepository;

    public ScoreService(GitHubService gitHubService,
                        LeetCodeService leetCodeService,
                        DeveloperProfileRepository developerProfileRepository) {
        this.gitHubService = gitHubService;
        this.leetCodeService = leetCodeService;
        this.developerProfileRepository = developerProfileRepository;
    }

    public DeveloperProfile refreshScore(User user) {

        // 1 - Fetch GitHub stats
        Map<String, Integer> github =
                gitHubService.getGitHubStats(user.getGithubUsername());

        // 2 - Fetch LeetCode stats only if username is linked
        Map<String, Integer> leetcode = Map.of(
                "easy", 0, "medium", 0, "hard", 0, "total", 0
        );
        if (user.getLeetcodeUsername() != null &&
                !user.getLeetcodeUsername().isEmpty()) {
            leetcode = leetCodeService.getLeetCodeStats(
                    user.getLeetcodeUsername()
            );
        }

        // 3 - Compute score
        // The weights reflect how hard each contribution is
        double score = 0;
        score += github.get("repos")    * 2;   // 1 repo    = 2 pts
        score += github.get("stars")    * 5;   // 1 star    = 5 pts
        score += leetcode.get("easy")   * 5;   // 1 easy    = 5 pts
        score += leetcode.get("medium") * 10;  // 1 medium  = 10 pts
        score += leetcode.get("hard")   * 20;  // 1 hard    = 20 pts

        // 4 - Save to DB
        Optional<DeveloperProfile> existing =
                developerProfileRepository.findByUser(user);

        DeveloperProfile profile = existing.orElse(new DeveloperProfile());
        profile.setUser(user);
        profile.setGithubRepos(github.get("repos"));
        profile.setGithubStars(github.get("stars"));
        profile.setGithubCommits(github.get("commits"));
        profile.setLeetcodeEasy(leetcode.get("easy"));
        profile.setLeetcodeMedium(leetcode.get("medium"));
        profile.setLeetcodeHard(leetcode.get("hard"));
        profile.setLeetcodeSolved(leetcode.get("total"));
        profile.setReputationScore(score);
        profile.setLastUpdated(LocalDateTime.now());

        return developerProfileRepository.save(profile);
    }
}