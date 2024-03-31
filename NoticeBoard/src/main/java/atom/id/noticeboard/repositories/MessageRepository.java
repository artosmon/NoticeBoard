package atom.id.noticeboard.repositories;

import atom.id.noticeboard.domains.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,String> {

    List<Message> findAllByTopicId(String topicId, PageRequest pageRequest);
    List<Message> findAllByTopicId(String topicId);
}
