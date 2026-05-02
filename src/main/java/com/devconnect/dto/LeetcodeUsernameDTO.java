package com.devconnect.dto;

import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

public class LeetcodeUsernameDTO {
    private String leetcodeUsername;

    public String getLeetcodeUsername() {return leetcodeUsername;}

    public void setLeetcodeUsername(String leetcodeUsername) {
        this.leetcodeUsername = leetcodeUsername;
    }
}
