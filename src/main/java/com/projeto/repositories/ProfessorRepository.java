package com.projeto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}
