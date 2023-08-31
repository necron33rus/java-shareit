package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;


    @NotBlank(message = "Название не указано")
    private String name;

    @NotBlank(message = "Описание не указано")
    private String description;

    @NotNull
    private Boolean available;
    private User owner;
    private Long requestId;
    private NearByBooking lastBooking;
    private NearByBooking nextBooking;
    private List<CommentDto> comments;

    @Data
    @Builder
    public static class NearByBooking {
        private Long id;
        private Long bookerId;
    }


}
