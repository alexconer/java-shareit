package ru.practicum.shareit.request.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @JoinColumn(name = "requestor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requestor;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "request_items",
        joinColumns = @JoinColumn(name = "request_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    @Column(name = "created")
    private LocalDateTime created;
}
