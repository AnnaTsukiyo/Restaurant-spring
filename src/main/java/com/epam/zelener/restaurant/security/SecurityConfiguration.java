package com.epam.zelener.restaurant.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(GET, "/api/login/**", "/api/dish/get/**", "/api/dish/all","/api/dish/sort/by/**","/api/dish/get/by/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/api/user/get/**","/api/user/all","/api/user/all/**", "/api/manager/all","/api/manager/**","/api/manager/get/name/**","/api/food/all","/api/food/get/**","/api/ingredient/all","/api/ingredient/get/**","/api/recipe/all", "/api/recipe/get/**","/api/order/all").hasAnyAuthority("MANAGER");
        http.authorizeRequests().antMatchers(GET, "/api/user/get/**","/api/user/all","/api/manager/all","/api/manager/**","/api/manager/get/name/**","/api/food/all","/api/food/get/**","/api/order/get/**").hasAnyAuthority("MANAGER", "CUSTOMER");
        http.authorizeRequests().antMatchers(POST, "/api/user/create","/api/order/create").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/dish/create", "/api/ingredient/create","/api/manager/create","/api/food/create","/api/recipe/create").hasAnyAuthority("MANAGER");
        http.authorizeRequests().antMatchers(PATCH, "/api/dish/update/**","/api/food/**","/api/ingredient/update/**","/api/manager/update/**","/api/order/update/**", "/api/recipe/update/**").hasAnyAuthority("MANAGER");
        http.authorizeRequests().antMatchers(PATCH, "/api/user/update/**").hasAnyAuthority("MANAGER", "CUSTOMER");
        http.authorizeRequests().antMatchers(DELETE, "/api/dish/deactivate/**", "/api/food/deactivate/**","/api/ingredient/deactivate/**","/api/manager/deactivate/**","/api/recipe/deactivate/**","/api/user/deactivate/**").hasAnyAuthority("MANAGER");
        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
