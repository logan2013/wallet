package com.blockchain.wallet.api.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author shujun  WebMvcConfigurer
 * @date 2018/6/26
 */
@EnableWebMvc
@Configuration
public class CustomerWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Bean
    AclInterceptor localInterceptor() {
        return new AclInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localInterceptor()).addPathPatterns("/**");
    }
}
