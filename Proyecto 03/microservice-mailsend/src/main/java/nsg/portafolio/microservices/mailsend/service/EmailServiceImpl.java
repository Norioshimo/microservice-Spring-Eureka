package nsg.portafolio.microservices.mailsend.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.internet.MimeMessage;
import nsg.portafolio.microservices.mailsend.service.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService {


    @Autowired
    private Dotenv dotenv;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMail(String[] toUser, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(dotenv.get("EMAIL_USER"));// Origen
        mailMessage.setTo(toUser);// Destino
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Override
    public void sendEmailWithFile(String[] toUser, String subject, String message, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, String.valueOf(StandardCharsets.UTF_8));

            mimeMessageHelper.setFrom(dotenv.get("EMAIL_USER"));
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
