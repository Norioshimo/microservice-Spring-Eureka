package nsg.portafolio.microservices.course.service.interfaces;

import java.util.List;
import nsg.portafolio.microservices.course.model.Course;
import nsg.portafolio.microservices.course.http.response.StudentByCourseResponse;

public interface ICourseService {

    List<Course> findAll();

    Course findById(Long id);

    void save(Course course);

    StudentByCourseResponse findStudentByIdCourse(Long idCourse);
}
