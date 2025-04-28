package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {

    private final UserService userService;
    private final EntityManager em;

    @Test
    void addUserTest() {
        UserDto userDto = getUserDto("user1", "user1@mail.ru");
        userService.addUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", userDto.getEmail());
        User user = query.getSingleResult();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void getUserTest() {
        UserDto newUserDto = getUserDto("user2", "user2@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        UserDto userDto = userService.getUserById(newUserDto.getId());
        assertEquals(userDto, newUserDto);
    }

    @Test
    void addWrongUserTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        userService.addUser(newUserDto);

        UserDto theSameUserDto = getUserDto("user1", "user1@mail.ru");

        assertThrows(Exception.class, () -> userService.addUser(theSameUserDto));
    }

    @Test
    void updateUserTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        UserDto updateUserDto = getUserDto("user1_updated", "user1_updated@mail.ru");
        updateUserDto.setId(newUserDto.getId());
        userService.updateUser(newUserDto.getId(), updateUserDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", updateUserDto.getEmail());
        User user = query.getSingleResult();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(updateUserDto.getName());
        assertThat(user.getEmail()).isEqualTo(updateUserDto.getEmail());
    }

    @Test
    void deleteUserTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        userService.deleteUser(newUserDto.getId());

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", newUserDto.getEmail());
        assertThrows(Exception.class, () -> query.getSingleResult());
    }

    private UserDto getUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }
}
