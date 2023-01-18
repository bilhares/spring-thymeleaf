package com.projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.projeto.service.EmailService;

@SpringBootApplication
public class RegescwebApplication implements CommandLineRunner {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("root"));
		SpringApplication.run(RegescwebApplication.class, args);
	}

//	@Autowired
//	JavaMailSender sender;

//	@Autowired
//	EmailService service;
	
	@Override
	public void run(String... args) throws Exception {
//		SimpleMailMessage simple = new SimpleMailMessage();
//		simple.setTo("sandbox.bilhares@gmail.com");
//		simple.setText("Testando envio de email");
//		simple.setSubject("Teste 1");
//
//		sender.send(simple);
		
//		service.enviarPedidoDeConfirmacaoDeCadastro("", "8Lk23U");

	}

}
