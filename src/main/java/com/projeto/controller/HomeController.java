package com.projeto.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

	@GetMapping("/solicitar/redefinir/senha")
	public String solicitarRedefinirSenha() {
		return "solicitar-redefinir-senha";
	}

	@GetMapping("/redefinir/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {

		usuarioService.pedidoRedefinicaoSenha(email);
		model.addAttribute("texto", "Voce recebera um email com o codigo verificador");
		model.addAttribute("titulo", "Solicitado");
		model.addAttribute("alerta", "sucesso");

		model.addAttribute("usuario", new Usuario(email));

		return "recuperar-senha";
	}

	@GetMapping("/expired")
	public String sessaoExpirada(ModelMap model) {

		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Acesso recusado");
		model.addAttribute("texto", "Sua sessão expirou");
		model.addAttribute("subtext", "Voce logou em outro dispositivo");
		return "login";
	}

	@PostMapping("/redefinir/senha/novo")
	public String confirmacaoDeRedefinicaoDeSenha(Usuario u, ModelMap model) {
		Usuario user = usuarioService.buscarPorEmail(u.getEmail());
		if (!user.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("texto", "Codigo verificador incorreto");
			model.addAttribute("titulo", "Codigo Errado");
			model.addAttribute("alerta", "erro");
			return "recuperar-senha";
		}

		user.setCodigoVerificador(null);
		user.setSenha(new BCryptPasswordEncoder().encode(u.getSenha()));

		usuarioRepository.save(user);
		model.addAttribute("texto", "Ok");
		model.addAttribute("titulo", "Senha redefinida");
		model.addAttribute("alerta", "sucesso");

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

	@GetMapping("/acesso-negado")
	public ModelAndView acessoNegado(HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("status", resp.getStatus());
		mv.addObject("error", "Acesso negado");
		mv.addObject("message", "Voce nao tem permissão.");

		return mv;
	}
}
