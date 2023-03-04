package com.gunnarro.web.config;

import com.gunnarro.web.endpoint.handler.AppSuccessHandler;
import com.gunnarro.web.endpoint.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
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
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        (requests) -> requests.requestMatchers(HttpMethod.GET, "/", "/public/**", "/index", "/site/**", "/webjars/**", "/css/**", "/js/**", "/images/**", "/svg/**", "/error/**", "/actuator/**").permitAll()
                                    .anyRequest()
                                    .authenticated())
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(this.accessDeniedHandler);
        return http.build();
    }
    // roles admin allow access to: /admin/**
    // roles user allow access to: /user/**
    // custom 403 access denied handler
    /*
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .antMatchers("/", "/public/**", "/index", "/site/**", "/webjars/**", "/css/**", "/js/**", "/images/**", "/svg/**", "/error/**", "/actuator/**").permitAll()
                        .antMatchers("/admin/**").hasAnyRole("ADMIN")
                        .antMatchers("/rest/**").hasAnyRole("USER")
                        .antMatchers("/**").hasAnyRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(this.accessDeniedHandler);

        return http.build();
    }

     */
}
