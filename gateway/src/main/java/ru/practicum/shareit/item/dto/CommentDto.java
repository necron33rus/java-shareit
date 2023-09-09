package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Long id;
    @NotNull
    @NotBlank
    private String text;
    private String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;
}