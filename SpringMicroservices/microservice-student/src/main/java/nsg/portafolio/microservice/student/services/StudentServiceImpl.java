package nsg.portafolio.microservice.student.services;

import nsg.portafolio.microservice.student.services.interfaces.IStudentService;
import java.util.List;
import nsg.portafolio.microservice.student.model.Student;
import nsg.portafolio.microservice.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public List<Student> findByIdCourse(Long idCourse) {

        return studentRepository.findAllByCourseId(idCourse);
    }

}
