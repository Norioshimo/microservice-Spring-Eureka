package nsg.portafolio.microservices.course.http.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nsg.portafolio.microservices.course.dto.StudentDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentByCourseResponse {

    private String courseName;
    private String teacher;
    private List<StudentDTO> studentDTOList;
    
    
}
