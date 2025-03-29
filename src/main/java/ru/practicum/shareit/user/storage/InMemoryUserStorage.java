package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.helper.CollectionHelper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User create(User user) {
        user.setId(CollectionHelper.getNextId(users));
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteById(long userId) {
        users.remove(userId);
    }
}
