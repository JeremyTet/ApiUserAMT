package com.example.apiuseramt.security;

import com.example.apiuseramt.security.jwt.UserAuthTokenFilter;
import com.example.apiuseramt.security.jwt.AuthEntryPointJwt;
import com.example.apiuseramt.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final UserAuthService adminAuthenticationService;

    @Autowired
    WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserAuthService adminAuthenticationService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Bean
    public UserAuthTokenFilter authAdminJwtTokenFilter() {
        return new UserAuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(adminAuthenticationService).passwordEncoder(passwordEncoder());
    }

    @Bean(name = "admin_manager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authAdminJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}


