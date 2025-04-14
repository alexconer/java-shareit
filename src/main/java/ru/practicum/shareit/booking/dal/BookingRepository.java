package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.booker = :booker order by p.start desc")
    Collection<Booking> findAllByBooker(User booker);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.booker = :booker and p.start < :now and p.end > :now order by p.start desc")
    Collection<Booking> findAllCurrentByBooker(User booker, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.booker = :booker and p.start > :now order by p.start desc")
    Collection<Booking> findAllFutureByBooker(User booker, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.booker = :booker and p.end < :now order by p.start desc")
    Collection<Booking> findAllPastByBooker(User booker, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.booker = :booker and p.status = :status order by p.start desc")
    Collection<Booking> findAllByBookerAndStatus(User booker, BookingStatus status);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item.owner = :owner order by p.start desc")
    Collection<Booking> findAllByItemOwner(User owner);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item.owner = :owner and p.start < :now and p.end > :now order by p.start desc")
    Collection<Booking> findAllCurrentByItemOwner(User owner, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item.owner = :owner and p.end < :now order by p.start desc")
    Collection<Booking> findAllPastByItemOwner(User owner, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item.owner = :owner and p.start > :now order by p.start desc")
    Collection<Booking> findAllFutureByItemOwner(User owner, LocalDateTime now);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item.owner = :owner and p.status = :status order by p.start desc")
    Collection<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus status);

    @Query("select p from Booking p JOIN FETCH p.booker JOIN FETCH p.item where p.item = :item and p.booker = :booker and p.start < :now order by p.start desc")
    Collection<Booking> findAllPastApprovedByItemAndBooker(Item item, User booker, LocalDateTime now);

    Collection<Booking> findAllByItemOrderByStartAsc(Item item);
}
