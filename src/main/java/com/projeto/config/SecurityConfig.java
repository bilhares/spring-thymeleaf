package com.projeto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.projeto.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**").permitAll().antMatchers("/", "/home").permitAll()
				.antMatchers("/novo-cadastro/**").permitAll().antMatchers("/confirmacao/cadastro/**").permitAll()
				.antMatchers("/solicitar/redefinir/senha").permitAll().antMatchers("/redefinir/senha").permitAll()
				.antMatchers("/redefinir/senha/novo").permitAll().antMatchers("/expired").permitAll()

				.antMatchers("/usuario/**").hasAuthority("ADMIN").antMatchers("/perfil/**").hasAuthority("USUARIO")
				.antMatchers("/professores/*/delete", "/professores/new", "/professores/*/edit")
				.hasAnyAuthority("ADMIN").antMatchers(HttpMethod.POST, "/professores").hasAnyAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/professores/*").hasAnyAuthority("ADMIN")

				.anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/")
				.failureUrl("/login-error").permitAll()
				.and()
				.logout()
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
				
				.and().exceptionHandling().accessDeniedPage("/acesso-negado").and().rememberMe();

		http.sessionManagement().maximumSessions(1).expiredUrl("/expired").maxSessionsPreventsLogin(false)
				.sessionRegistry(sessionRegistry());

		http.sessionManagement().sessionFixation().newSession().sessionAuthenticationStrategy(sessionAuthStrategy());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
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
