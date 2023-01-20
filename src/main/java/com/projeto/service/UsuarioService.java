package com.projeto.service;

import java.util.List;
import java.util.Optional;

import jakarta.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.projeto.model.Perfil;
import com.projeto.model.Usuario;
import com.projeto.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	EmailService emailService;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailAtivo(String email) {
		return usuarioRepository.findByEmailAndAtivo(email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis())));
	}

	private String[] getAuthorities(List<Perfil> perfis) {
		String[] auths = new String[perfis.size()];

		for (int i = 0; i < perfis.size(); i++) {
			auths[i] = perfis.get(i).getDesc();
		}

		return auths;
	}

	@Transactional(readOnly = false)
	public void ativarCadastro(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));

		Usuario u = buscarPorEmail(email);
		if (u.getId() == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		u.setAtivo(true);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		String verificador = RandomStringUtils.randomAlphanumeric(6);

		usuario.setCodigoVerificador(verificador);

		emailService.enviarPedidoDeRedefinicaoDeSenha(email, verificador);
	}
}
