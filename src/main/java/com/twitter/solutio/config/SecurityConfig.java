package com.twitter.solutio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        //Create 10 in memory users (user1 to user10)
        List<UserDetails> userDetailsList = new ArrayList<>();
        for (int i=1; i<11; i++) {
            UserDetails user = User
                    .withUsername("user" + i)
                    .password("$2y$12$gW3z8T1o7MQNhARbe3tTRud0NtkWjg5MCq7dOOw4vRTTyNdW5/YPa")
                    .roles("USER")
                    .build();
            userDetailsList.add(user);
        }
        return new InMemoryUserDetailsManager(userDetailsList);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/h2-console/**",
                "/swagger-ui.html",
                "/swagger-ui/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/swagger*/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable();
    }


}
