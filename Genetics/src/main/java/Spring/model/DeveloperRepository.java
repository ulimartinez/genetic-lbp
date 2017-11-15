package Spring.model;

import Spring.entity.Developer;
import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository extends CrudRepository<Developer, Long> {

}
