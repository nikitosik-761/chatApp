package com.chatApp.chatApp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AuthHelper {

    public static String getAuthUserId(){
        String id = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof String)) {

            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            id = oauth2User.getAttribute("sub");

        }

        return id;
    }









}
