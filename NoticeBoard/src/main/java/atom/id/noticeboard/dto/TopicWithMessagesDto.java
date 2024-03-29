package atom.id.noticeboard.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicWithMessagesDto {

    String id;
    String name;
    Instant created;
    List<MessageDto> messages;
}
