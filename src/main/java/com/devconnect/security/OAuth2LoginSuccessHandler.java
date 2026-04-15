package com.devconnect.security;

import com.devconnect.entity.DeveloperProfile;
import com.devconnect.entity.User;
import com.devconnect.repository.DeveloperProfileRepository;
import com.devconnect.repository.UserRepository;
//import io.jsonwebtoken.Jwt;
//import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final DeveloperProfileRepository developerProfileRepository ;
    private final JwtUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

//        Step 1 --> Get the GitHub user info
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    //        String email = oAuth2User.getAttribute("email");
    //        String name = oAuth2User.getAttribute("name");
    //        String githubUsername = oAuth2User.getAttribute("gitHubUsername");
    //        String avatarUrl = oAuth2User.getAttribute("avatar_url");

              String githubUsername = oAuth2User.getAttribute("login");
              String name = githubUsername  ;
              if(oAuth2User.getAttribute("name")!=null){
                  name = oAuth2User.getAttribute("name");
              }
              String avatarUrl = oAuth2User.getAttribute("avatar_url");
              String email = githubUsername+"@github.com";

//        Step 2 --> Checking if youser exist phle se or not ?

        Optional<User> existingUser = userRepository.findByGithubUsername(githubUsername);

        User user ;

        if(existingUser.isPresent())
        {
//            User is already present toh sirf update kar do uske reocrd ko

            user = existingUser.get();
            user.setName(name);
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

        } else {
//            HEEHEHE --> AB TOH NAYA USER AA GYA

            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setGithubUsername(githubUsername);
            user.setAvatarUrl(avatarUrl);
            user.setRole(User.Role.USER);
            userRepository.save(user);


//            Also create an empty DEveloperProfile for them

            DeveloperProfile profile = new DeveloperProfile();
            profile.setUser(user);
            developerProfileRepository.save(profile);


        }
//        Step 3 --> Genraating our wt token
        String token = jwtUtil.generateToken(user.getEmail());

//        Step 4 --> Send token back in the response
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"token\":\"" +token +"\"," +
                        "\"username\": \"" + githubUsername + "\", " +
                        "\"name\": \"" +githubUsername + "\" ,}"
        );


    }
}
