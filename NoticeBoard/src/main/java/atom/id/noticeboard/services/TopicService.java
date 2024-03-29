package atom.id.noticeboard.services;


import atom.id.noticeboard.FactoryDto.MappingTopicUtils;
import atom.id.noticeboard.domains.Message;
import atom.id.noticeboard.domains.Topic;
import atom.id.noticeboard.dto.NewTopicDto;
import atom.id.noticeboard.dto.TopicDto;
import atom.id.noticeboard.dto.TopicWithMessagesDto;
import atom.id.noticeboard.exceptions.TopicHasNoMessagesException;
import atom.id.noticeboard.exceptions.InvalidInputException;
import atom.id.noticeboard.exceptions.NotFoundException;
import atom.id.noticeboard.repositories.TopicRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TopicService {

    TopicRepository topicRepository;
    MessageService messageService;
    MappingTopicUtils mappingTopicUtils;
    public TopicWithMessagesDto saveNewTopic(NewTopicDto newTopicDto) {
        if(newTopicDto.getTopicName() != null && !newTopicDto.getTopicName().isEmpty()
                && newTopicDto.getMessage() != null) {
            Topic topic = mappingTopicUtils.mapToTopic(newTopicDto);
            topicRepository.save(topic);
            log.info("Topic:\"{}\" has been successfully saved", topic.getName());
            return mappingTopicUtils.mapToTopicWithMessagesDto(topic);
        }
        else {
            throw new InvalidInputException("Invalid input");
        }
    }

    public TopicWithMessagesDto updateTopic(TopicDto topic) {
        if(topicRepository.existsById(topic.getId())) {
            topicRepository.save(mappingTopicUtils.mapToTopic(topic));
            return getTopicWithMessages(topic.getId());
        }
        else {
            throw new NotFoundException("Topic not found");
        }
    }


    public List<TopicDto> getAllTopics() {

        List<Topic> topics = Optional.of(
                topicRepository.findAll()).orElseGet(ArrayList::new);

        return topics.stream().map(mappingTopicUtils::mapToTopicDto).collect(Collectors.toList());
    }

    public TopicWithMessagesDto getTopicWithMessages(String id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new NotFoundException("Invalid topic ID"));
        List<Message> messages = messageService.getMessagesByTopicId(id);

        if(messages.isEmpty())
            throw new TopicHasNoMessagesException("");
        topic.setMessages(messages);

        return mappingTopicUtils.mapToTopicWithMessagesDto(topic);
    }

}
