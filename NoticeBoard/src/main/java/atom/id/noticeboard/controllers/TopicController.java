package atom.id.noticeboard.controllers;

import atom.id.noticeboard.dto.MessageDto;
import atom.id.noticeboard.dto.NewTopicDto;
import atom.id.noticeboard.dto.TopicDto;
import atom.id.noticeboard.dto.TopicWithMessagesDto;
import atom.id.noticeboard.exceptions.TopicHasNoMessagesException;
import atom.id.noticeboard.exceptions.InvalidInputException;
import atom.id.noticeboard.exceptions.NotFoundException;
import atom.id.noticeboard.services.MessageService;
import atom.id.noticeboard.services.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class TopicController {

    TopicService topicService;
    MessageService messageService;

    @PostMapping("/topic")
    public ResponseEntity<?> saveTopic(@RequestBody NewTopicDto topic) {
        try {
            return ResponseEntity.ok(topicService.saveNewTopic(topic));
        }catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/topic")
    public ResponseEntity<?> updateTopic(@RequestBody TopicDto topic) {

        try {
            topicService.updateTopic(topic);
            return ResponseEntity.ok(topicService.updateTopic(topic));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/topic")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<?> getMessages(@PathVariable String topicId) {
        try {
            return ResponseEntity.ok(topicService.getTopicWithMessages(topicId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TopicHasNoMessagesException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/topic/{topicId}/message")
    public ResponseEntity<?> createMessage(@PathVariable String topicId,
                                                              @RequestBody MessageDto message) {
        try {
           return ResponseEntity.ok(messageService.saveNewMessage(message,topicId));
        } catch (NotFoundException e) {
           return ResponseEntity.notFound().build();
        } catch (InvalidInputException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/topic/{topicId}/message")
    public ResponseEntity<?> updateMessage(@PathVariable String topicId,
                              @RequestBody MessageDto message) {
        try {
            return ResponseEntity.ok(messageService.updateMessage(message,topicId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String messageId) {
        try {
            messageService.deleteMessage(messageId);
            return ResponseEntity.status(204).build();

        } catch (TopicHasNoMessagesException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
