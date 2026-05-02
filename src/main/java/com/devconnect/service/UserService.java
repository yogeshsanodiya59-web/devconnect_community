package com.devconnect.service;

import com.devconnect.dto.UserDTO;
import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.User;
import com.devconnect.repository.DeveloperProfileRepository;
import com.devconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DeveloperProfileRepository developerProfileRepository;

    public UserService(UserRepository userRepository,
                       DeveloperProfileRepository developerProfileRepository) {
        this.userRepository = userRepository;
        this.developerProfileRepository = developerProfileRepository;
    }

//    Covertingg the USer + developer file wwala area into -0-> UserDTO

    public UserDTO convertToDTO(User user){
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setGithubUsername(user.getGithubUsername());
        dto.setLeetcodeUsername(user.getLeetcodeUsername());
        dto.setRole(user.getRole().name());

//        Get their dEVELOPER PROOFILE STATS
        Optional<DeveloperProfile> profileOpt =
                developerProfileRepository.findByUser(user);
        if(profileOpt.isPresent()){

//            Yeh saare frintend se firect calll nh ho iss liye using DTO
            DeveloperProfile profile = profileOpt.get();
            dto.setReputationScore(profile.getReputationScore());
            dto.setGithubRepos(profile.getGithubRepos());
            dto.setGithubCommits(profile.getGithubCommits());
            dto.setGithubStars(profile.getGithubStars());
            dto.setLeetcodeSolved(profile.getLeetcodeSolved());
            dto.setLeetcodeEasy(profile.getLeetcodeEasy());
            dto.setLeetcodeMedium(profile.getLeetcodeMedium());
            dto.setLeetcodeHard(profile.getLeetcodeHard());

        }
        return  dto;

    }

//    Get Logged in users's profile !!!!!]]
    public UserDTO getMyProfile(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not founf BHaiya ji "));
        return convertToDTO(user);
    }

//    Gettingg any user's getPubicPorfile by githubusername
    public UserDTO getPublicProfile(String githubUsername){
        User user = userRepository.findByGithubUsername(githubUsername)
                .orElseThrow(()-> new RuntimeException("User NOT FOUND"));
    return convertToDTO(user);
    }

    // Save leetcode username
    public UserDTO linkLeetcode(String email, String leetcodeUsername) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLeetcodeUsername(leetcodeUsername);
        userRepository.save(user);
        return convertToDTO(user);
    }

}
