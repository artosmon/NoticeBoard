package atom.id.noticeboard.repositories;

import atom.id.noticeboard.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByName(String username);
}
