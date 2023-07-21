package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotNull
    @NotBlank(message = "название не указано")
    private String name;

    @NotBlank(message = "название не указано")
    private String description;

    @NotNull
    private Boolean available;
    private User owner;
    private Long incomingRequest;
}
