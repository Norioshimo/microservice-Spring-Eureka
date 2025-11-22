package nsg.portafolio.microservices.course.repository;

import nsg.portafolio.microservices.course.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
