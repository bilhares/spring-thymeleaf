package com.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.model.PerfilTipo;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@GetMapping("/novo")
	public ModelAndView cadastroPorAdminParaUsuario() {
		ModelAndView mv = new ModelAndView("usuarios/cadastro");
		mv.addObject("listaPerfis", PerfilTipo.values());
		return mv;
	}
}
