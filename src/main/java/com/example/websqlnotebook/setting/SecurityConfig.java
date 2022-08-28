package com.example.websqlnotebook.setting;

import com.example.websqlnotebook.service.SecurityService;
import com.example.websqlnotebook.service.impl.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Autowired
    //public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    //}

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()).permitAll()
                //для незарегистрированных
                .antMatchers("/registration").not().fullyAuthenticated()
                //для админов
                .antMatchers("/admin/**").hasRole("ADMIN")
                //для юзеров
                //.antMatchers("/opoks/**", "/uploads/**", "/logons/**").hasRole("USER")
                //для всех
                .antMatchers("/", "/resources/**").permitAll()
                //.anyRequest().fullyAuthenticated()
                .anyRequest().authenticated()
                //.and().httpBasic()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and().logout().permitAll()
                .logoutSuccessUrl("/")
        ;
    }

}
