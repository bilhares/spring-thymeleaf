package com.projeto.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.dto.NovoAlunoDTO;
import com.projeto.model.Aluno;
import com.projeto.repositories.AlunoRepository;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

	@Autowired
	AlunoRepository alunoRepository;

	@GetMapping()
	public ModelAndView index(NovoAlunoDTO alunoDto) {
		List<Aluno> alunos = alunoRepository.findAll();
		ModelAndView mv = new ModelAndView("alunos/alunos");
		mv.addObject("alunos", alunos);
		return mv;
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(@Valid NovoAlunoDTO alunoDto, BindingResult bidingResult) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("alunos/alunos");
			List<Aluno> alunos = alunoRepository.findAll();
			mv.addObject("alunos", alunos);
			return mv;
		} else {
			Aluno p = new Aluno();
			BeanUtils.copyProperties(alunoDto, p);
			alunoRepository.save(p);
			return new ModelAndView("redirect:/alunos");
		}
	}

	@GetMapping("/{id}/edit")
	public ModelAndView preEdit(@PathVariable Long id) {
		Aluno aluno = alunoRepository.findById(id).get();
		ModelAndView mv = new ModelAndView("alunos/alunos");

		NovoAlunoDTO alunoDto = new NovoAlunoDTO();
		BeanUtils.copyProperties(aluno, alunoDto);

		mv.addObject("novoAlunoDTO", alunoDto);
		mv.addObject("alunos", alunoRepository.findAll());
		mv.addObject("alunoId", aluno.getId());

		return mv;
	}

	@PostMapping("/{id}")
	public ModelAndView edit(@PathVariable Long id, @Valid NovoAlunoDTO alunoDto, BindingResult bidingResult) {
		if (bidingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("alunos/alunos");
			mv.addObject("alunos", alunoRepository.findAll());
			return mv;
		} else {
			Aluno aluno = alunoRepository.findById(id).get();
			BeanUtils.copyProperties(alunoDto, aluno);
			alunoRepository.save(aluno);
			return new ModelAndView("redirect:/alunos");
		}
	}

}
