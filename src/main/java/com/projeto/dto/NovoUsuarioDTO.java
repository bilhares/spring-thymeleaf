package com.projeto.dto;

import java.util.List;

import com.projeto.model.Perfil;

public class NovoUsuarioDTO {

	private String email;
	private String senha;
	private List<Perfil> perfis;

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
