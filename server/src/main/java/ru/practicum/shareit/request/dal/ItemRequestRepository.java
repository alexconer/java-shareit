package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT i FROM ItemRequest i LEFT JOIN FETCH i.items WHERE i.requestor = :user")
    Collection<ItemRequest> findAllByRequestor(User user);

    @Query("SELECT i FROM ItemRequest i WHERE i.requestor != :user ORDER BY i.created DESC")
    Collection<ItemRequest> findAllByOrderByCreatedDesc(User user);

    @Override
    @Query("SELECT i FROM ItemRequest i LEFT JOIN FETCH i.items WHERE i.id = :requestId")
    Optional<ItemRequest> findById(Long requestId);
}
