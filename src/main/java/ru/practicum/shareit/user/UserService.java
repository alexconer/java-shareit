package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    public UserDto addUser(UserDto userDto) {
        if (userStorage.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicatedDataException("Пользователь с таким email уже существует");
        }

        return UserMapper.toUserDto(userStorage.create(UserMapper.toUserModel(userDto)));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> user = userStorage.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User oldUser = user.get();

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            Optional<User> userByEmail = userStorage.findByEmail(userDto.getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(oldUser.getId())) {
                throw new DuplicatedDataException("Пользователь с таким email уже существует");
            }
            oldUser.setEmail(userDto.getEmail());
        }
        userStorage.update(id, oldUser);
        return UserMapper.toUserDto(oldUser);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userStorage.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.deleteById(id);
    }
}
