package nsg.portafolio.microservices.mailsend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailFileDTO {
    private String[] toUser;
    private String subject;
    private String message;
    private MultipartFile file;
}
