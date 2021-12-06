// package com.ecomerce.udemy.ecomerce.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import
// org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// @Order(2)
// public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

// @Autowired
// private UserDetailsService userDetailsService;

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// @Override
// protected void configure(AuthenticationManagerBuilder auth) throws Exception
// {
// auth

// .userDetailsService(userDetailsService)
// .passwordEncoder(passwordEncoder());
// }

// @Override
// protected void configure(HttpSecurity http) throws Exception {
// http
// .authorizeRequests()
// .antMatchers("/category/**").hasAnyRole("USER", "ADMIN")
// .antMatchers("/admin/**").hasAnyRole("ADMIN")
// .antMatchers("/").permitAll()
// .and()
// .formLogin()
// .loginPage("/login")
// .and()
// .logout()
// .logoutSuccessUrl("/")
// .and()
// .exceptionHandling()
// .accessDeniedPage("/");

// // .antMatchers("/**").hasAnyRole("USER");
// // http
// // .authorizeRequests()
// // .antMatchers("/category/**").access("hasRole('ROLE_USER')")
// // .antMatchers("/").access("permitAll");
// }
// }