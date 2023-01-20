package com.projeto.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	SpringTemplateEngine template;

	public void enviarPedidoDeConfirmacaoDeCadastro(String destino, String codigo) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();
		context.setVariable("titulo", "Bem vindo ao REGESCWEB");
		context.setVariable("texto", "Precisamos que confirme seu cadastro com o link abaixo.");
		context.setVariable("linkConfirmacao", "http://localhost:8080/confirmacao/cadastro?codigo=" + codigo);

		String html = template.process("email/confirmarcao", context);

		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Confirmacao de cadastro");
		helper.setFrom("nao-responder@regescweb.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);
	}

	public void enviarPedidoDeRedefinicaoDeSenha(String destino, String verificador) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();
		context.setVariable("titulo", "Redefinicao de senha");
		context.setVariable("texto", "Para redefinir sua senha utilize o codigo abaixo");
		context.setVariable("verificador", verificador);

		String html = template.process("email/confirmarcao", context);

		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Redefinicao de senha");
		helper.setFrom("nao-responder@regescweb.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);
	}

}
