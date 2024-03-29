package atom.id.noticeboard.services;


import atom.id.noticeboard.FactoryDto.MappingTopicUtils;
import atom.id.noticeboard.domains.Message;
import atom.id.noticeboard.domains.Topic;
import atom.id.noticeboard.dto.MessageDto;
import atom.id.noticeboard.dto.TopicWithMessagesDto;
import atom.id.noticeboard.exceptions.InvalidInputException;
import atom.id.noticeboard.exceptions.NotFoundException;
import atom.id.noticeboard.exceptions.TopicHasNoMessagesException;
import atom.id.noticeboard.repositories.MessageRepository;
import atom.id.noticeboard.repositories.TopicRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MessageService {

    MessageRepository messageRepository;
    TopicRepository topicRepository;
    MappingTopicUtils mappingTopicUtils;

    public TopicWithMessagesDto saveNewMessage(MessageDto dto, String topicId) {
        if(!dto.getAuthor().isEmpty() && !dto.getText().isEmpty() && dto.getCreated() != null) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException("Invalid topic ID"));
            Message message = mappingTopicUtils.mapToMessage(dto, topic);
            messageRepository.save(message);
            return mappingTopicUtils.mapToTopicWithMessagesDto(topic);
        }
        else {
            throw new InvalidInputException("Message has invalid input");
        }

    }

    public TopicWithMessagesDto updateMessage(MessageDto dto, String topicId) {

        if(!dto.getAuthor().isEmpty() && !dto.getText().isEmpty() && dto.getCreated() != null) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() ->
                    new NotFoundException("Invalid topic ID"));

            messageRepository.findById(dto.getId()).orElseThrow(() ->
                    new NotFoundException("invalid message ID"));

            Message message = mappingTopicUtils.mapToMessage(dto,topic);
            messageRepository.save(message);
            return mappingTopicUtils.mapToTopicWithMessagesDto(topic);
        } else {
            throw new InvalidInputException("Message has invalid input");
        }
    }

    public void deleteMessage(@NotNull String id) {
        Message message = messageRepository.findById(id).orElseThrow();
        if(topicRepository.findById(message.getTopic().getId()).get().getMessages().size() <= 1)
            throw new TopicHasNoMessagesException("Topic should not have less than 1 messages");

        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesByTopicId(String topicId) {
        return Optional.of(messageRepository.findAllByTopicId(topicId)).orElseGet(ArrayList::new);
    }
}
