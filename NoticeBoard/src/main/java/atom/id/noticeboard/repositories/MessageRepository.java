package atom.id.noticeboard.repositories;

import atom.id.noticeboard.domains.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,String> {

    List<Message> findAllByTopicId(String topicId, PageRequest pageRequest);
    List<Message> findAllByTopicId(String topicId);

    @Modifying
    @Query("delete from Message where id = ?1")
    void deleteById(String id);
}
