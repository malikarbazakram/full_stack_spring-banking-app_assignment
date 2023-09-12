package com.redmath.mybankingapplication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity
@Configuration
public class WebConfiguration {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/h2-console/", "GET"), // Allow GET requests to h2-console
                new AntPathRequestMatcher("/h2-console/", "POST"),
                new AntPathRequestMatcher("/h2-console/**", "GET"), // Allow GET requests to h2-console
                new AntPathRequestMatcher("/h2-console/**", "POST"),
                new AntPathRequestMatcher("/actuator", "GET"),
                new AntPathRequestMatcher("/actuator/**", "GET")
//                new AntPathRequestMatcher("/api/v1/**", "GET"),
//                new AntPathRequestMatcher("/api/v1/**", "POST"),
//                new AntPathRequestMatcher("/api/v1/**", "DELETE"),
//                new AntPathRequestMatcher("/api/v1/**", "PUT")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.formLogin(formLogin -> Customizer.withDefaults());
        http.formLogin(formLogin->formLogin.defaultSuccessUrl("http://localhost:3000/", true));
        http.authorizeHttpRequests(config -> config.anyRequest().authenticated());
//        http.csrf(csrf -> csrf.disable());

        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepository.setCookiePath("/");
        http.csrf(config -> config.csrfTokenRepository(csrfRepository)
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));

        return http.build();
    }

}