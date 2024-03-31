package atom.id.noticeboard.dto;


import atom.id.noticeboard.domains.Message;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewTopicDto {

    String topicName;
    Message message;

    @Builder.Default
    Instant createdAt = Instant.now();

}
