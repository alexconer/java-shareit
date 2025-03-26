package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();
    Optional<User> findById(long userId);
    Optional<User> findByEmail(String email);
    User create(User user);
    User update(long userId, User user);
    void deleteById(long userId);
}
