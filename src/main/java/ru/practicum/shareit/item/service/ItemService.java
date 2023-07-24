package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserOwnershipException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto create(ItemDto itemDto, long userId) {
        return ItemMapper.toItemDto(itemStorage.create(
                ItemMapper.toItem(
                        itemDto,
                        UserMapper.toUser(checkUserExist(userId))
                )
        ));
    }

    public ItemDto update(ItemDto itemDto, long itemId, long userId) {
        var updatedItem = checkIfExists(itemStorage.findById(itemId));
        if (Optional.ofNullable(updatedItem.getOwner()).isPresent() && updatedItem.getOwner().getId() != userId) {
            throw new UserOwnershipException("User with id=" + userId +
                    " is not the owner of the item with id=" + itemId);
        }
        if (Optional.ofNullable(itemDto.getName()).isPresent()) {
            updatedItem.setName(itemDto.getName());
        }
        if (Optional.ofNullable(itemDto.getDescription()).isPresent()) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (Optional.ofNullable(itemDto.getAvailable()).isPresent()) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        updatedItem.setOwner(UserMapper.toUser(checkUserExist(userId)));
        itemStorage.update(updatedItem);

        return ItemMapper.toItemDto(updatedItem);
    }

    public ItemDto findById(long itemId) {
        return ItemMapper.toItemDto(checkIfExists(itemStorage.findById(itemId)));
    }

    public List<ItemDto> findAllByUserId(long userId) {
        return findAllItemDtoByFilter(item -> item.getOwner() != null && item.getOwner().getId() == userId);
    }

    public List<ItemDto> searchByText(String text) {
        return text.isEmpty() ? Collections.emptyList() :
                findAllItemDtoByFilter(
                        item -> (StringUtils.containsIgnoreCase(item.getName(), text)
                                || StringUtils.containsIgnoreCase(item.getDescription(), text)
                                && item.getAvailable())
                );
    }

    private List<ItemDto> findAllItemDtoByFilter(Predicate<Item> filter) {
        return itemStorage.findAll().stream()
                .filter(filter)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private <T> T checkIfExists(T obj) {
        if (obj == null) {
            throw new NotFoundException("Item not exists");
        }
        return obj;
    }

    private UserDto checkUserExist(long userId) {
        return Optional.ofNullable(userStorage.findById(userId))
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not exists"));
    }
}
