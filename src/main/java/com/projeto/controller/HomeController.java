package com.projeto.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.dto.NovoUsuarioDTO;
import com.projeto.model.PerfilTipo;
import com.projeto.model.Usuario;
import com.projeto.repositories.UsuarioRepository;
import com.projeto.service.EmailService;
import com.projeto.service.UsuarioService;

@Controller
public class HomeController {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping({ "/", "/home" })
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/novo-cadastro")
	public String novoCadastro(NovoUsuarioDTO dto) {
		return "register";
	}

	@PostMapping("/novo-cadastro/salvar")
	public String novoCadastroSalvar(NovoUsuarioDTO dto, BindingResult result) {

		try {
			String crypt = new BCryptPasswordEncoder().encode(dto.getSenha());
			Usuario u = new Usuario();

			u.setEmail(dto.getEmail());
			u.setSenha(crypt);
			u.setAtivo(false);
			u.addPerfil(PerfilTipo.USUARIO);

			usuarioRepository.save(u);

			String codigo = Base64Utils.encodeToString(dto.getEmail().getBytes());
			emailService.enviarPedidoDeConfirmacaoDeCadastro(dto.getEmail(), codigo);

		} catch (DataIntegrityViolationException e) {
			result.reject("email", "esse email ja existe");
			return "register";
		} catch (MessagingException e) {
			System.err.println(e.getMessage());
		}

		return "redirect:/login";
	}

	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastro(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
		usuarioService.ativarCadastro(codigo);

		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo", "cadastro ativado");
		attr.addFlashAttribute("texto", "parabéns seu cadastro esta ativo.");

		return "redirect:/login";
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
