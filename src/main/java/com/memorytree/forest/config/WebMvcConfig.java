package com.memorytree.forest.config;

import com.memorytree.forest.exception.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserIdArgumentResolver userIdArgumentResolver;

    public WebMvcConfig(UserIdArgumentResolver userIdArgumentResolver) {
        this.userIdArgumentResolver = userIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }
}
