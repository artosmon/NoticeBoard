package atom.id.noticeboard.repositories;

import atom.id.noticeboard.domains.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,String> {
}
