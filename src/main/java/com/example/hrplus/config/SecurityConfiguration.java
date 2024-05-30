package com.example.hrplus.config;

import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                // Allow all requests to the "/images" folder for your login page image
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/employees/add").permitAll()
                .requestMatchers("/employees/edit").permitAll()

                // Any other request requires authentication
                .anyRequest().authenticated()
        );
        http.formLogin(form -> form
                .loginPage("/login")
                .permitAll());
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
