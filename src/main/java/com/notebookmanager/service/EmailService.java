package com.notebookmanager.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(String emailDestinatario, String assunto, String mensagem) {

        SimpleMailMessage novoEmail = new SimpleMailMessage();
        novoEmail.setFrom("caiocsgandara@gmail.com");
        novoEmail.setTo(emailDestinatario);
        novoEmail.setSubject(assunto);
        novoEmail.setText(mensagem); 
        emailSender.send(novoEmail);
    
    }

    public String criaMsgEmprestimo(String nomeDestinatario, String patrimonioNotebook) {
        return "Esta é uma mensagem automática.\n\n" +
        "Atestamos que o notebook de patrimônio " + patrimonioNotebook + " foi retirado por empréstimo do Centro de Apoio Didático por " +
        nomeDestinatario + " em " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm")) +
        " nas condições descritas no termo de utilização e responsabilidade abaixo. Caso haja alguma objeção, favor responder este e-mail " +
        "ou entrar em contato no ramal 6908 imediatamente.\n\n";
    }

    public String criaMsgDevolucao(String nomeDestinatario, String patrimonioNotebook) {
        return "Esta é uma mensagem automática.\n\n" +
        "Atestamos que o notebook de patrimônio " + patrimonioNotebook + " foi devolvido ao Centro de Apoio Didático por " +
        nomeDestinatario + " em " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm")) +
        " nas condições descritas no termo de utilização e responsabilidade.\n\n";
    }
    
}
