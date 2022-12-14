package com.projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.model.Perfil;
import com.projeto.model.Usuario;
import com.projeto.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmail(username);

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
}
