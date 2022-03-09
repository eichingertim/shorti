package me.timeichinger.shortiservice.service.impl;

import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.repositories.UserRepository;
import me.timeichinger.shortiservice.service.UserService;
import me.timeichinger.shortiservice.utils.error.ErrorCode;
import me.timeichinger.shortiservice.utils.requests.UserRequest;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User createUser(UserRequest userRequest) throws ShortUrlException {

        User user = userRepository.findUserByUsername(userRequest.getUsername());
        if (user != null) throw new ShortUrlException(ErrorCode.USER_ALREADY_EXISTS);

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(encoder.encode(userRequest.getPassword()));
        newUser.setEmail(userRequest.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public User getUser(String id) throws ShortUrlException {
        if (id == null) {
            throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_EDIT_USER);
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new ShortUrlException(ErrorCode.USER_NOT_FOUND);
        return user.get();
    }

    @Override
    public void deleteUser(String id) throws ShortUrlException {

        if (id == null) {
            throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_EDIT_USER);
        }

        userRepository.deleteById(id);
    }

}
