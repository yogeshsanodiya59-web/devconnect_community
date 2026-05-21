package com.devconnect.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class LeetCodeService {

    private final WebClient webClient;

    public LeetCodeService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://leetcode.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Map<String, Integer> getLeetCodeStats(String leetcodeUsername) {
        try {
            String query = """
                {
                  "query": "{ matchedUser(username: \\"%s\\") { submitStats { acSubmissionNum { difficulty count } } } }"
                }
                """.formatted(leetcodeUsername);

            Map response = webClient.post()
                    .uri("/graphql")
                    .bodyValue(query)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            int easy = 0, medium = 0, hard = 0;

            if (response != null && response.get("data") != null) {
                Map data = (Map) response.get("data");
                Map matchedUser = (Map) data.get("matchedUser");

                if (matchedUser != null) {
                    Map submitStats = (Map) matchedUser.get("submitStats");
                    List<Map> acSubmissions =
                            (List<Map>) submitStats.get("acSubmissionNum");

                    for (Map submission : acSubmissions) {
                        String difficulty = submission.get("difficulty").toString();
                        int count = Integer.parseInt(
                                submission.get("count").toString()
                        );
                        switch (difficulty) {
                            case "Easy"   -> easy   = count;
                            case "Medium" -> medium = count;
                            case "Hard"   -> hard   = count;
                        }
                    }
                }
            }

            return Map.of(
                    "easy", easy,
                    "medium", medium,
                    "hard", hard,
                    "total", easy + medium + hard
            );

        } catch (Exception e) {
            System.out.println("LeetCode API error: " + e.getMessage());
            return Map.of("easy", 0, "medium", 0, "hard", 0, "total", 0);
        }
    }
}