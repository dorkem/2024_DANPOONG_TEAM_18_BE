package com.memorytree.forest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class OAuth2LoginController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2LoginController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/login/oauth2/code/kakao")
    public RedirectView oauth2Login(HttpServletRequest request, HttpServletResponse response,
                                    @RegisteredOAuth2AuthorizedClient(value = "kakao", registrationId = "kakao") OAuth2AuthorizedClient authorizedClient,
                                    Authentication authentication) throws IOException {
        if (authorizedClient == null) {
            // If the client is not authorized, redirect to the authorization page
            return new RedirectView("/oauth2/authorization/kakao");
        }

        // Access token retrieval after authentication
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // Redirecting with ID in header
        String kakaoUserId = authorizedClient.getPrincipalName();
        response.addHeader("Kakao-User-Id", kakaoUserId);
        return new RedirectView("/api/v1/home");
    }
}
