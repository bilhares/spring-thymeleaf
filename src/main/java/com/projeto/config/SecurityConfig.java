package com.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated());

		http.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).failureUrl("/login").permitAll());

		http.logout(logout -> logout.logoutSuccessUrl("/"));
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/", "/home", "/css/**");
	}
}
