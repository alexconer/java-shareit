package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;

    @Test
    void addItemTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        ItemDto newItemDto = getItemDto("item1", "description1", true);
        newItemDto = itemService.addItem(newUserDto.getId(), newItemDto);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        query.setParameter("id", newItemDto.getId());
        Item item = query.getSingleResult();

        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo(newItemDto.getName());
        assertThat(item.getDescription()).isEqualTo(newItemDto.getDescription());
        assertThat(item.getAvailable()).isEqualTo(newItemDto.getAvailable());
        assertThat(item.getOwner()).isEqualTo(newUserDto.getId());
    }

    @Test
    void getItemByIdTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        ItemDto newItemDto = getItemDto("item1", "description1", true);
        newItemDto = itemService.addItem(newUserDto.getId(), newItemDto);

        ItemWithBookingDto itemDto = itemService.getItemById(newUserDto.getId(), newItemDto.getId());

        assertThat(itemDto.getId()).isNotNull();
        assertThat(itemDto.getName()).isEqualTo(newItemDto.getName());
        assertThat(itemDto.getDescription()).isEqualTo(newItemDto.getDescription());
        assertThat(itemDto.getAvailable()).isEqualTo(newItemDto.getAvailable());
    }

    private UserDto getUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

    private ItemDto getItemDto(String name, String description, Boolean available) {
        return ItemDto.builder()
                .name(name)
                .description(description)
                .available(available)
                .build();
    }
}
