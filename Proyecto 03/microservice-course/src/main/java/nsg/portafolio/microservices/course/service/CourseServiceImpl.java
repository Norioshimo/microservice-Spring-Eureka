package nsg.portafolio.microservices.course.service;

import nsg.portafolio.microservices.course.service.interfaces.ICourseService;
import java.util.List;
import nsg.portafolio.microservices.course.client.StudentClient;
import nsg.portafolio.microservices.course.dto.StudentDTO;
import nsg.portafolio.microservices.course.model.Course;
import nsg.portafolio.microservices.course.http.response.StudentByCourseResponse;
import nsg.portafolio.microservices.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentClient studentClient;

    @Override
    public List<Course> findAll() {

        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public StudentByCourseResponse findStudentByIdCourse(Long idCourse) {

        // Consultar el curso
        Course course = courseRepository.findById(idCourse).orElse(new Course());

        // Obtener los estudiantes
        List<StudentDTO> studentDTOList = studentClient.findAllStudentByCourse(idCourse);

        return StudentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();

    }

}
