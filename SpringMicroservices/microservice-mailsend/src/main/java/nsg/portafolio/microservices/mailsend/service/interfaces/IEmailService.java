package nsg.portafolio.microservices.mailsend.service.interfaces;

import java.io.File;

public interface IEmailService {

    void sendMail(String[] toUser,String subject,String message);


    void sendEmailWithFile(String[] toUser, String subject, String message, File file);
}
