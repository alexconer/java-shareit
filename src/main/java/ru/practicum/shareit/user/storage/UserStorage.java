package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public Collection<User> findAll();
    public Optional<User> findById(long userId);
    public Optional<User> findByEmail(String email);
    public User create(User user);
    public User update(long userId, User user);
    public void deleteById(long userId);
}
