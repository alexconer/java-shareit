package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public Collection<ItemDto> getAllItemsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public ItemWithBookingDto getItemById(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean isUserOwner = user.getId().equals(item.getOwner());

        ItemWithBookingDto result = ItemMapper.toItemWithBookingDto(item);

        Collection<Booking> bookings = bookingRepository.findAllByItemOrderByStartAsc(item);

        boolean isUserBooker = false;
        for (Booking booking : bookings) {
            if (isUserOwner && booking.getEnd().isBefore(LocalDateTime.now())) {
                result.setLastBooking(BookingMapper.toBookingDto(booking));
            }
            if (isUserOwner && booking.getStart().isAfter(LocalDateTime.now())) {
                result.setNextBooking(BookingMapper.toBookingDto(booking));
            }
            if (booking.getBooker().getId().equals(userId)) {
                isUserBooker = true;
            }
        }

        if (!isUserBooker && !isUserOwner) {
            throw new AccessDeniedException("Вещь не доступна для просмотра ");
        }

        List<CommentDto> comments = commentRepository.findAllByItem(item).stream()
                .map(CommentMapper::toCommentDto)
                .toList();

        result.setComments(comments);

        return result;
    }

    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItemModel(itemDto);
        item.setOwner(user.getId());
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!user.getId().equals(oldItem.getOwner())) {
            throw new AccessDeniedException("Вещь не принадлежит пользователю");
        }

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }

        itemRepository.save(oldItem);

        return ItemMapper.toItemDto(oldItem);
    }

    public Collection<ItemDto> searchItems(Long userId, String text) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        Booking booking = bookingRepository.findAllPastApprovedByItemAndBooker(item, user, LocalDateTime.now()).stream().findFirst().orElseThrow(() -> new ValidationException("Пользователь не брал вещь в аренду"));

        if (!booking.getStatus().equals(BookingStatus.APPROVED) || booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Пользователь не брал вещь в аренду");
        }

        Comment comment = CommentMapper.toCommentModel(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
