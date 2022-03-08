package me.timeichinger.shortiservice.repositories;

import me.timeichinger.shortiservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findUserByUsername(String username);
}
