package com.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("perfil")
public class PerfilController {

	@GetMapping()
	public ModelAndView verDadosDoPerfil() {
		ModelAndView mv = new ModelAndView("perfil/detalhes");
		return mv;
	}

}
