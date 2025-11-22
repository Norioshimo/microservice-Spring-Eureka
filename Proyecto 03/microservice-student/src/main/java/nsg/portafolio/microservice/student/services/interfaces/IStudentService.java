package nsg.portafolio.microservice.student.services.interfaces;

import java.util.List;
import nsg.portafolio.microservice.student.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudentService {

    List<Student> findAll();

    Student findById(Long id);

    void save(Student student);

    List<Student> findByIdCourse(Long idCourse);
}
