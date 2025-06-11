package nsg.portafolio.microservices.mailsend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import nsg.portafolio.microservices.mailsend.domain.EmailDTO;
import nsg.portafolio.microservices.mailsend.domain.EmailFileDTO;
import nsg.portafolio.microservices.mailsend.service.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private IEmailService emailService;

    @GetMapping("/")
    public ResponseEntity<?> getHelloWord() {
        return (ResponseEntity<?>) ResponseEntity.ok().body("hola mundo");
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO) {

        System.out.println("Mensaje recibido. " + emailDTO);

        emailService.sendMail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());

        Map<String, String> response = new HashMap<String, String>();
        response.put("estado", "Enviado correctamente");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendMailFile")
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFileDTO emailFileDTO) {

        try {
            String fileName = Paths.get(emailFileDTO.getFile().getOriginalFilename()).getFileName().toString();

            Path path = Paths.get("src/mail/resources/files/" + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(emailFileDTO.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            File file = path.toFile();

            emailService.sendEmailWithFile(emailFileDTO.getToUser(), emailFileDTO.getSubject(), emailFileDTO.getMessage(), file);

            Map<String, String> response = new HashMap<String, String>();
            response.put("estado", "Enviado correctamente");
            response.put("archivo", "fileName");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el Email con el archivo. " + e.getMessage());
        }

    }
}
