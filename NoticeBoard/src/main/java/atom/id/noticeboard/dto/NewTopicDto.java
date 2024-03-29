package atom.id.noticeboard.dto;


import atom.id.noticeboard.domains.Message;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewTopicDto {

    String topicName;
    Message message;

}
