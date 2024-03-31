package atom.id.noticeboard.services;


import atom.id.noticeboard.domains.User;
import atom.id.noticeboard.exceptions.InvalidInputException;
import atom.id.noticeboard.exceptions.UserAlreadyExistsException;
import atom.id.noticeboard.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        if(user.getName().isEmpty() || user.getPassword().isEmpty()) {
            throw new InvalidInputException("Username and password must not be empty");
        }
        if(userRepository.findByName(user.getName()).isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        else {
            throw new UserAlreadyExistsException();
        }
    }




}
