package nsg.portafolio.microservice.student.repository;

import java.util.List;
import nsg.portafolio.microservice.student.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.courseId = :idCourse")
    List<Student> findAllStudent(Long idCourse);

    List<Student> findAllByCourseId(Long idCourse);
}
