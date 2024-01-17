package com.gunnarro.web.config;

import com.gunnarro.web.endpoint.handler.AppSuccessHandler;
import com.gunnarro.web.endpoint.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author admin
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private AppSuccessHandler successHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    /**
     * public SecurityWebFilterChain securityWebFilterChain(
     * ServerHttpSecurity http) {
     * return http.authorizeExchange()
     * .pathMatchers("/actuator/**").permitAll()
     * .anyExchange().authenticated()
     * .and().build();
     * }
     */
    /*
    .authorizeHttpRequests(autorizeRequests -> autorizeRequests
      .requestMatchers(HttpMethod.GET, "/ws/healthz", "/ws/ready", "/ws/version").permitAll()
      .requestMatchers(HttpMethod.GET,
        "/ws/user/**",
        "/ws/user/avatar/*",
        "/ws/user/search").hasAnyAuthority("SCOPE_tmt:user")
      .requestMatchers(HttpMethod.POST,
        "/ws/friend",
        "/ws/user/trip",
        "/ws/trip/*").hasAnyAuthority("SCOPE_tmt:user")
     */


    // roles admin allow access to: /admin/**
    // roles user allow access to: /user/**
    // custom 403 access denied handler
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/", "/resources/**", "/public/**", "/static/**", "/index", "/site/**", "/webjars/**", "/css/**", "/js/**", "/images/**", "/svg/**", "/error/**", "/actuator/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/rest/**").hasRole("USER")
                                .requestMatchers("/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
