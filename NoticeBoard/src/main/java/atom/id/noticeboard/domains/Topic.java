package atom.id.noticeboard.domains;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @Builder.Default
    String id = UUID.randomUUID().toString();

    String name;

    @Builder.Default
    Instant createdAt = Instant.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic",cascade = CascadeType.ALL)
    @Builder.Default
    List<Message> messages = new ArrayList<>();
}
