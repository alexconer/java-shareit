package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicatedDataException("Пользователь с таким email уже существует");
        }

        return UserMapper.toUserDto(userRepository.save(UserMapper.toUserModel(userDto)));
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User oldUser = user.get();

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(oldUser.getId())) {
                throw new DuplicatedDataException("Пользователь с таким email уже существует");
            }
            oldUser.setEmail(userDto.getEmail());
        }
        userRepository.save(oldUser);
        return UserMapper.toUserDto(oldUser);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }
}
