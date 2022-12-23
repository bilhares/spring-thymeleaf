package com.projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.model.Usuario;
import com.projeto.repositories.UsuarioRepository;

@Controller
@RequestMapping("perfil")
public class PerfilController {

	@Autowired
	UsuarioRepository usuarioRepository;

	@GetMapping()
	public ModelAndView verDadosDoPerfil(@AuthenticationPrincipal User user) {
		ModelAndView mv = new ModelAndView("perfil/detalhes");
		Usuario u = usuarioRepository.findByEmail(user.getUsername());
		mv.addObject("user", u);
		return mv;
	}

}
