package com.devconnect.service;


import org.hibernate.annotations.SecondaryRow;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final WebClient webClient;
    public GitHubService(){
        this.webClient =WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept " , "application/vnd.github.v3+json")
                .build();
    }

    public Map<String ,Integer> getGitHubStats(String githubUsername){
        try{

            List<Map> repos = webClient.get()
                    .uri("/users/" + githubUsername + "/repos?per_page=100")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
            int totalRepos = 0 ;
            int totalStars = 0 ;

            if(repos!= null){
                totalRepos = repos.size();
                for(Map repo : repos){
                    Object stars = repo.get("stargrazers_count");
                    if(stars != null){
                        totalStars += Integer.parseInt(stars.toString());
                    }
                }
            }

            return Map.of(

                    "repos", totalRepos,
                    "stars", totalStars,
                    "commits", 0
            );



        }
        catch (Exception e) {
            System.out.println("GitHub API error: " + e.getMessage());
            return Map.of("repos", 0, "stars", 0, "commits", 0);
        }
    }
    }

