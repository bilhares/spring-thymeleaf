package com.projeto.dto;

import jakarta.validation.constraints.NotBlank;

public class NovoAlunoDTO {
	@NotBlank
	private String nome;

	@NotBlank
	private String cpf;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
