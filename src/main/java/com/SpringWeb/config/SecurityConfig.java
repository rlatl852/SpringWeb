package com.SpringWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.SpringWeb.security.CuntomLogoutSuccessHandler;
import com.SpringWeb.security.CustomloginFailureHandler;
import com.SpringWeb.security.CustomloginSuccessHandler;
import com.SpringWeb.service.CustomUserDetailService;

import lombok.extern.log4j.Log4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
		
		http.authorizeRequests()
			.antMatchers("/css/**", "/js/**").permitAll()
			.antMatchers("/Replies/new").permitAll()
			.antMatchers("/Replies").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
			.antMatchers("/Board/write").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
			.antMatchers("/Board/modify").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
			.antMatchers("/Board/remove").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')");
			
			
		
		System.out.println("formLoginPage Setting.............");
		
		http.formLogin()
			.loginPage("/member/login")
			.loginProcessingUrl("/SpringWeb/login")
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailureHandler());
		
		http.logout()
		.logoutUrl("/SpringWeb/logout")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSION_ID")
		.logoutSuccessHandler(logoutSuccessHandler());
	}
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new CustomloginSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new CustomloginFailureHandler();
	}
	
	@Bean
	public UserDetailsService customUserService() {
		return new CustomUserDetailService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CuntomLogoutSuccessHandler();
	}
}
