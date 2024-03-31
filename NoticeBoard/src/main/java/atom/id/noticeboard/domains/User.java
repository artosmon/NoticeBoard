package atom.id.noticeboard.domains;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Builder.Default
    String id = UUID.randomUUID().toString();

    String name;

    String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Role role = Role.USER;
}
