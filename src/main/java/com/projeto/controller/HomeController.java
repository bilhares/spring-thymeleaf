package com.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping({ "/", "/home" })
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public ModelAndView loginError() {
		ModelAndView mv = new ModelAndView("login");
		mv.addObject("alerta", "erro");
		mv.addObject("titulo", "Credenciais inválidas");
		mv.addObject("texto", "Login ou senha incorretos");
		mv.addObject("subtexto", "Necessário estar logado para acessar os recursos");

		return mv;
	}
}
