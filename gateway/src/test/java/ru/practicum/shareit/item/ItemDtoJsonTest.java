package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testItemDtoJson() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .available(true)
                .build();

        assertThat(json.write(itemDto)).hasJsonPath("$.id");
        assertThat(json.write(itemDto)).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json.write(itemDto)).hasJsonPath("$.name");
        assertThat(json.write(itemDto)).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(json.write(itemDto)).hasJsonPath("$.description");
        assertThat(json.write(itemDto)).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(json.write(itemDto)).hasJsonPath("$.available");
        assertThat(json.write(itemDto)).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

    @Test
    void testJsonToItemDto() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .available(true)
                .build();

        String jsonText = """
            {"id":1,"name":"Test","description":"Test","available":true}
        """;

        ItemDto parsedItemDto = json.parseObject(jsonText);

        assertThat(parsedItemDto.getId()).isEqualTo(itemDto.getId());
        assertThat(parsedItemDto.getName()).isEqualTo(itemDto.getName());
        assertThat(parsedItemDto.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(parsedItemDto.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Test
    void testInvalidItemDtoJson() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name"));

        itemDto.setName("Test");
        violations = validator.validate(itemDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("description"));

        itemDto.setDescription("Test");
        violations = validator.validate(itemDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("available"));

        itemDto.setAvailable(true);
        violations = validator.validate(itemDto);
        assertThat(violations).isEmpty();
    }

}
