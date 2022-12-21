package com.projeto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}
