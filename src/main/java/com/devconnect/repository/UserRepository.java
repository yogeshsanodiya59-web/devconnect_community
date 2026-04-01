package com.devconnect.repository;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User ,Long> {

        Optional<User> findByGithubUsername(String githubUsername);

        Optional<User> findByEmail(String email);

}
