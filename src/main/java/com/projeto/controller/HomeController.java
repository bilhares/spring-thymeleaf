package com.projeto.controller;

import javax.servlet.http.HttpServletResponse;

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
	
	@GetMapping("/novo-cadastro")
	public String novoCadastro() {
		return "register";
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

	@GetMapping("/acesso-negado")
	public ModelAndView acessoNegado(HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("status", resp.getStatus());
		mv.addObject("error", "Acesso negado");
		mv.addObject("message", "Voce nao tem permissão.");

		return mv;
	}
}
