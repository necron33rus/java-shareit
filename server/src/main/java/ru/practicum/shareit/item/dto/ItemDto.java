package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;

    private String name;

    private String description;

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
