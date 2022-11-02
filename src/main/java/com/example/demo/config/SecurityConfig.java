package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.repository.SiteUserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
	
	private final SiteUserRepository userRepository; 
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain fillterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(auth -> 
			auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()			
			.mvcMatchers("/register","/login").permitAll()
			.mvcMatchers("/admin/**").hasAnyAuthority(Authority.ADMIN.name())
			.anyRequest().authenticated()
		).formLogin(login -> login
				.loginPage("/login").defaultSuccessUrl("/").permitAll()
				)
		.logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
				)
		.rememberMe().key("Unique and Secret");
		
		return http.build();
	}
	
	
	
	
}
