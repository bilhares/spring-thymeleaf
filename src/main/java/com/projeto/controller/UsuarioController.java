package com.projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.dto.NovoUsuarioDTO;
import com.projeto.model.Usuario;
import com.projeto.repositories.PerfilRepository;
import com.projeto.repositories.UsuarioRepository;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	PerfilRepository perfilRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@GetMapping("/novo")
	public ModelAndView cadastroPorAdminParaUsuario(NovoUsuarioDTO dto) {
		ModelAndView mv = new ModelAndView("usuarios/cadastro");
		mv.addObject("listaPerfis", perfilRepository.findAll());
		return mv;
	}

	@PostMapping("/salvar")
	public ModelAndView cadastrarUsuario(NovoUsuarioDTO dto) {
		ModelAndView mv = new ModelAndView("usuarios/cadastro");
		mv.addObject("listaPerfis", perfilRepository.findAll());

		String crypt = new BCryptPasswordEncoder().encode(dto.getSenha());
		Usuario u = new Usuario();

		u.setEmail(dto.getEmail());
		u.setSenha(crypt);
		u.setAtivo(true);
		u.setPerfis(dto.getPerfis());

		usuarioRepository.save(u);

		return mv;
	}
}
