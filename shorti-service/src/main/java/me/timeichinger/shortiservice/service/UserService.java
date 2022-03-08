package me.timeichinger.shortiservice.service;

import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.utils.requests.UserRequest;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;

public interface UserService {
    User createUser(UserRequest userRequest) throws ShortUrlException;
    User getUser(String id) throws ShortUrlException;
    User updateUser(String id, User user) throws ShortUrlException;
    void deleteShortUrl(String id);


}
