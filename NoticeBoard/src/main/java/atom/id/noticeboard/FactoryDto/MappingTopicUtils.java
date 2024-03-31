package atom.id.noticeboard.FactoryDto;

import atom.id.noticeboard.domains.Message;
import atom.id.noticeboard.domains.Topic;
import atom.id.noticeboard.domains.User;
import atom.id.noticeboard.dto.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MappingTopicUtils {

    public Topic mapToTopic(NewTopicDto dto) {
        Topic topic = Topic.builder()
                .name(dto.getTopicName())
                .createdAt(dto.getCreatedAt())
                .build();
        dto.getMessage().setTopic(topic);
        topic.getMessages().add(dto.getMessage());
        return topic;
    }

    public TopicDto mapToTopicDto(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .created(topic.getCreatedAt())
                .build();
    }

    public Topic mapToTopic(TopicDto dto) {
        return Topic.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreated())
                .build();
    }


    public TopicWithMessagesDto mapToTopicWithMessagesDto(Topic topic) {
        return TopicWithMessagesDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .created(topic.getCreatedAt())
                .messages(
                        topic.getMessages().stream().map(this::mapToMessageDto).collect(Collectors.toList())
                )
                .build();
    }

    public MessageDto mapToMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .author(message.getAuthor())
                .text(message.getText())
                .created(message.getCreated())
                .build();
    }

    public Message mapToMessage(MessageDto dto,Topic topic) {
        Message message = Message.builder()
                .topic(topic)
                .author(dto.getAuthor())
                .text(dto.getText())
                .created(dto.getCreated())
                .build();
        if(dto.getId() != null) {
            message.setId(dto.getId());
        }
        return message;
    }


}
