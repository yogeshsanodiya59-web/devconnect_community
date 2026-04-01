package com.devconnect.repository;

import com.devconnect.entity.User;
import com.devconnect.entity.DeveloperProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.Optional;

public interface DeveloperProfileRepository extends JpaRepository<DeveloperProfile , Long > {
    Optional<DeveloperProfile > findByUser(User user);
}
