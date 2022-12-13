package com.projeto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.dto.NovoProfessorDTO;
import com.projeto.model.Professor;
import com.projeto.model.StatusProfessor;
import com.projeto.repositories.ProfessorRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

	@Autowired
	ProfessorRepository professorRepository;

	@GetMapping()
	public ModelAndView index() {

		List<Professor> ps = professorRepository.findAll();
		ModelAndView mv = new ModelAndView("professores/index");
		mv.addObject("professores", ps);

		return mv;
	}

	@GetMapping("/new")
	public ModelAndView newProfessor(NovoProfessorDTO novoProfessorDTO) {
		ModelAndView mv = new ModelAndView("professores/new");
		mv.addObject("listaStatus", StatusProfessor.values());
		return mv;
	}

	@PostMapping()
	public ModelAndView createProfessor(@Valid NovoProfessorDTO novoProfessorDTO, BindingResult bidingResult) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("professores/new");
			mv.addObject("listaStatus", StatusProfessor.values());
			return mv;
		} else {

			Professor p = new Professor();
			BeanUtils.copyProperties(novoProfessorDTO, p);
			professorRepository.save(p);
			return new ModelAndView("redirect:/professores/" + p.getId());
		}
	}

	@GetMapping("/{id}")
	public ModelAndView show(@PathVariable Long id) {
		Optional<Professor> optional = professorRepository.findById(id);

		if (optional.isPresent()) {
			ModelAndView mv = new ModelAndView("professores/show");
			mv.addObject("professor", optional.get());

			return mv;
		} else {
			return new ModelAndView("redirect:/professores");
		}
	}

	@GetMapping("/{id}/edit")
	public ModelAndView edit(@PathVariable Long id) {
		Optional<Professor> optional = professorRepository.findById(id);
		if (optional.isPresent()) {
			ModelAndView mv = new ModelAndView("professores/edit");

			NovoProfessorDTO novoProfessorDTO = new NovoProfessorDTO();
			BeanUtils.copyProperties(optional.get(), novoProfessorDTO);

			mv.addObject("novoProfessorDTO", novoProfessorDTO);
			mv.addObject("listaStatus", StatusProfessor.values());
			mv.addObject("professorId", optional.get().getId());
			return mv;
		} else {
			return new ModelAndView("redirect:/professores");
		}
	}

	@PostMapping("/{id}")
	public ModelAndView update(@PathVariable Long id, @Valid NovoProfessorDTO novoProfessorDTO,
			BindingResult bidingResult) {

		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("professores/edit");
			mv.addObject("professorId", id);
			mv.addObject("listaStatus", StatusProfessor.values());
			return mv;
		} else {
			Optional<Professor> optional = this.professorRepository.findById(id);

			if (optional.isPresent()) {
				Professor professor = optional.get();
				BeanUtils.copyProperties(novoProfessorDTO, professor);
				this.professorRepository.save(professor);

				return new ModelAndView("redirect:/professores/" + professor.getId());
			} else {
				System.out.println("############ NÃO ACHOU O PROFESSOR DE ID " + id + " ############");
				return null;
			}
		}
	}

	@GetMapping("/{id}/delete")
	public ModelAndView delete(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("redirect:/professores");
		try {
			professorRepository.deleteById(id);
			mv.addObject("mensagem", "Professor deletado com sucesso!");
			mv.addObject("erro", false);

		} catch (EmptyResultDataAccessException e) {
			mv = retornaErro("Erro o deletar " + e.getMessage());
		}
		return mv;
	}

	private ModelAndView retornaErro(String msg) {
		ModelAndView mv = new ModelAndView("redirect:/professores");
		mv.addObject("mensagem", msg);
		mv.addObject("erro", true);
		return mv;
	}
}
