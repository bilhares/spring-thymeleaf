package com.projeto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
				.antMatchers("/redefinir/senha/novo").permitAll()

				.antMatchers("/usuario/**").hasAuthority("ADMIN").antMatchers("/perfil/**").hasAuthority("USUARIO")
				.antMatchers("/professores/*/delete", "/professores/new", "/professores/*/edit")
				.hasAnyAuthority("ADMIN").antMatchers(HttpMethod.POST, "/professores").hasAnyAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/professores/*").hasAnyAuthority("ADMIN")

				.anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/")
				.failureUrl("/login-error").permitAll().and().logout().logoutSuccessUrl("/")

				.and().exceptionHandling().accessDeniedPage("/acesso-negado").and().rememberMe();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
