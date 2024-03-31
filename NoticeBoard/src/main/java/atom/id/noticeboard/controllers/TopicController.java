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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
        } catch (NotFoundException | InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/topic/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateTopicAsAdmin(@RequestBody TopicDto topic) {
        try {
            return ResponseEntity.ok(topicService.updateAnyTopic(topic));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/topic")
    public ResponseEntity<List<TopicDto>> getAllTopics(
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(topicService.getAllTopics(PageRequest.of(page, size)));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<?> getMessages(
            @PathVariable String topicId,
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(topicService.getMessagesOfTopic(topicId,PageRequest.of(page,size)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TopicHasNoMessagesException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/topic/{topicId}/message")
    public ResponseEntity<?> createMessage(
            @PathVariable String topicId,
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

    @PutMapping("/topic/{topicId}/message/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateMessageAsAdmin(@PathVariable String topicId,
                                           @RequestBody MessageDto message) {
        try {
            return ResponseEntity.ok(messageService.updateAnyMessage(message,topicId));
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

        } catch (TopicHasNoMessagesException | InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/message/{messageId}/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteMessageAsAdmin(@PathVariable String messageId) {

        try {
            log.info("delete admin");
            messageService.deleteAnyMessage(messageId);
            return ResponseEntity.status(204).body("message has been deleted");
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/topic/{topicId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicId) {
        try {
            topicService.deleteTopic(topicId);
            return ResponseEntity.ok().body("topic has been deleted");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("something went wrong...");
        }
    }



}
