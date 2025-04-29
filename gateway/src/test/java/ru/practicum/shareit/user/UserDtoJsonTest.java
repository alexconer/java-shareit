package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserDtoJson() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();

        assertThat(json.write(userDto)).hasJsonPath("$.id");
        assertThat(json.write(userDto)).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json.write(userDto)).hasJsonPath("$.name");
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json.write(userDto)).hasJsonPath("$.email");
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.email").isEqualTo("test@test.com");
    }

    @Test
    void testJsonToUserDto() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();

        String jsonText = "{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\"}";

        UserDto parsedUserDto = json.parseObject(jsonText);

        assertThat(parsedUserDto.getId()).isEqualTo(userDto.getId());
        assertThat(parsedUserDto.getName()).isEqualTo(userDto.getName());
        assertThat(parsedUserDto.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void testInvalidUserDtoJson() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name"));

        userDto.setName("Test");
        violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));

        userDto.setEmail("test");
        violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));

        userDto.setEmail("test@test.com");
        violations = validator.validate(userDto);

        assertThat(violations).isEmpty();
    }

}
