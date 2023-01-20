package com.projeto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.projeto.service.UsuarioService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/css/**").permitAll().requestMatchers("/", "/home")
				.permitAll().requestMatchers("/novo-cadastro/**").permitAll()
				.requestMatchers("/confirmacao/cadastro/**").permitAll().requestMatchers("/solicitar/redefinir/senha")
				.permitAll().requestMatchers("/redefinir/senha").permitAll().requestMatchers("/redefinir/senha/novo")
				.permitAll().requestMatchers("/expired").permitAll()

				.requestMatchers("/usuario/**").hasAuthority("ADMIN").requestMatchers("/perfil/**")
				.hasAuthority("USUARIO")
				.requestMatchers("/professores/*/delete", "/professores/new", "/professores/*/edit")
				.hasAnyAuthority("ADMIN").requestMatchers(HttpMethod.POST, "/professores").hasAnyAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/professores/*").hasAnyAuthority("ADMIN").anyRequest()
				.authenticated()).formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login-error")
				.permitAll().and().logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID")

				.and().exceptionHandling().accessDeniedPage("/acesso-negado").and().rememberMe();

		http.sessionManagement().maximumSessions(1).expiredUrl("/expired").maxSessionsPreventsLogin(false)
				.sessionRegistry(sessionRegistry());

		http.sessionManagement().sessionFixation().newSession().sessionAuthenticationStrategy(sessionAuthStrategy());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
			UsuarioService userDetailsService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder).and().build();
	}

	@Bean
	public SessionAuthenticationStrategy sessionAuthStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}
}
