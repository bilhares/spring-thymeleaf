package com.projeto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.projeto.model.StatusProfessor;

public class NovoProfessorDTO {

	@NotBlank
	private String nome;

	@NotNull
	@DecimalMin("0.0")
	private BigDecimal salario;
	private StatusProfessor statusProfessor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatusProfessor getStatusProfessor() {
		return statusProfessor;
	}

	public void setStatusProfessor(StatusProfessor statusProfessor) {
		this.statusProfessor = statusProfessor;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
}
