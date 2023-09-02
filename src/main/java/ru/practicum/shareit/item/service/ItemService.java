package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.UserOwnershipException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.booking.model.Booking;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final EntityUtils entityUtils;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    public ItemDto create(ItemDto itemDto, long userId) {
        var user = entityUtils.getUserIfExists(userId);
        if (itemDto.getRequestId() != null) {
            var request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow();
            var item = ItemMapper.toItem(itemDto, user);
            item.setRequest(request);
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, entityUtils.getUserIfExists(userId))));
    }

    @Transactional
    public ItemDto update(ItemDto itemDto, long itemId, long userId) {
        var updatedItem = entityUtils.getItemIfExists(itemId);
        if (Optional.ofNullable(updatedItem.getOwner()).isPresent() && updatedItem.getOwner().getId() != userId) {
            throw new UserOwnershipException("User with id=" + userId +
                    " is not the owner of the item with id=" + itemId);
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(updatedItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updatedItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updatedItem::setAvailable);

        updatedItem.setOwner(entityUtils.getUserIfExists(userId));
        itemRepository.save(updatedItem);

        return ItemMapper.toItemDto(updatedItem);
    }

    @Transactional(readOnly = true)
    public ItemDto findById(long itemId, long userId) {
        var item = entityUtils.getItemIfExists(itemId);
        var itemDto = ItemMapper.toItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            addBookings(itemDto);
        }

        addCommentsDto(itemDto);
        return itemDto;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findAllByUserId(long userId) {
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .map(this::addCommentsDto)
                .map(this::addBookings)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> searchByText(@NotNull String text) {
        return text.isEmpty() ? Collections.emptyList() :
                findAllItemDtoByFilter(
                        item -> (StringUtils.containsIgnoreCase(item.getName(), text)
                                || StringUtils.containsIgnoreCase(item.getDescription(), text)
                                && item.getAvailable())
                );
    }

    private List<ItemDto> findAllItemDtoByFilter(Predicate<Item> filter) {
        return itemRepository.findAll().stream()
                .filter(filter)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(long itemId, long userId, CommentDto commentDto) {
        if (isBookingExists(itemId, userId)) {
            throw new BadRequestException("User with id=" + userId + " never booked item with id=" + itemId);
        }

        var comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(entityUtils.getUserIfExists(userId));
        comment.setItem(entityUtils.getItemIfExists(itemId));
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private boolean isBookingExists(long itemId, long userId) {
        return bookingRepository.findAllByItemIdAndBookerId(itemId, userId).stream()
                .noneMatch(booking -> booking.getStatus() == BookingStatus.APPROVED
                        && booking.getStart().isBefore(LocalDateTime.now()));
    }

    private ItemDto addCommentsDto(ItemDto itemDto) {
        var comments = commentRepository.findAllByItemId(itemDto.getId());
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    private ItemDto addBookings(ItemDto itemDto) {
        var bookings = bookingRepository.findAllByItemId(itemDto.getId());

        var prevBooking = bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        var nextBooking = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);

        itemDto.setLastBooking(prevBooking != null ? ItemDto.NearByBooking.builder()
                .id(prevBooking.getId())
                .bookerId(prevBooking.getBooker().getId())
                .build() : null);

        itemDto.setNextBooking(nextBooking != null ? ItemDto.NearByBooking.builder()
                .id(nextBooking.getId())
                .bookerId(nextBooking.getBooker().getId())
                .build() : null);
        return itemDto;
    }
}
