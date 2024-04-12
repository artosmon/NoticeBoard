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
import atom.id.noticeboard.security.MyUserDetails;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MessageService {

    final MessageRepository messageRepository;

    final MappingTopicUtils mappingTopicUtils;

    @Setter
    TopicService topicService;


    public TopicWithMessagesDto saveNewMessage(MessageDto dto, String topicId) {
        if(!dto.getAuthor().isEmpty() && !dto.getText().isEmpty() && dto.getCreated() != null) {
            Topic topic = topicService.findTopicById(topicId);
            Message message = mappingTopicUtils.mapToMessage(dto, topic);
            messageRepository.save(message);
            return mappingTopicUtils.mapToTopicWithMessagesDto(topic);
        }
        else {
            throw new InvalidInputException("Message has invalid input");
        }

    }

    public TopicWithMessagesDto updateMessage(MessageDto dto, String topicId) {
        String getAuthUsername = MyUserDetails.getAuthUsername();
        if(!dto.getAuthor().equals(getAuthUsername))
            throw new InvalidInputException("This is not your message");
        return getUpdatedTopicWIthMessages(dto, topicId);
    }

    public TopicWithMessagesDto updateAnyMessage(MessageDto dto, String topicId) {

        return getUpdatedTopicWIthMessages(dto, topicId);
    }

    private TopicWithMessagesDto getUpdatedTopicWIthMessages(MessageDto dto, String topicId) {
        if(!dto.getAuthor().isEmpty() && !dto.getText().isEmpty() && dto.getCreated() != null) {
            Topic topic = topicService.findTopicById(topicId);

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Message message = messageRepository.findById(id).orElseThrow();
        if(!auth.getName().equals(message.getAuthor()))
            throw new InvalidInputException("This is not your message");

        if(topicService.findTopicById(message.getTopic().getId()).getMessages().size() <= 1)
            throw new TopicHasNoMessagesException("Topic should not have less than 1 messages");

        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesByTopicId(String topicId, PageRequest pageRequest) {

        return Optional.of(messageRepository.findAllByTopicId(topicId,pageRequest)).orElseGet(ArrayList::new);
    }

    public List<Message> getMessagesByTopicId(String topicId) {

        return Optional.of(messageRepository.findAllByTopicId(topicId)).orElseGet(ArrayList::new);
    }

    public void deleteAnyMessage(@NotNull String id) {
        Message message = messageRepository.findById(id).orElseThrow(() ->
                new InvalidInputException("message with this id was not found"));

        if(topicService.findTopicById(message.getTopic().getId()).getMessages().size() <= 1)
            topicService.deleteTopicById(message.getTopic().getId());

        messageRepository.deleteById(id);
    }
}
