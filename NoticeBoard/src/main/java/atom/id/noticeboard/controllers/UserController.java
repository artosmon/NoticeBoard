package atom.id.noticeboard.controllers;


import atom.id.noticeboard.FactoryDto.MappingTopicUtils;
import atom.id.noticeboard.domains.User;
import atom.id.noticeboard.exceptions.InvalidInputException;
import atom.id.noticeboard.exceptions.UserAlreadyExistsException;
import atom.id.noticeboard.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class UserController {

    UserService userService;
    MappingTopicUtils mappingTopicUtils;
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {

        try {
            return ResponseEntity.ok(userService.saveUser(user));
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
