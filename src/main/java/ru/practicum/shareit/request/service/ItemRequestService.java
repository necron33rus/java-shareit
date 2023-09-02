package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.utils.EntityUtils;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequestDto;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final EntityUtils entityUtils;

    public ItemRequestDto create(ItemRequestDto itemRequestDto, long userId) {
        var user = entityUtils.getUserIfExists(userId);
        var itemRequest = toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);

        var itemFromRepository = itemRequestRepository.save(itemRequest);
        return toItemRequestDto(itemFromRepository, null);

    }

    public List<ItemRequestDto> getListRequestsByUser(long userId) {
        entityUtils.getUserIfExists(userId);
        return itemRequestRepository.findAllByRequesterId(userId).stream()
                .map(itemRequest -> toItemRequestDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public ItemRequestDto findById(long id, long userId) {
        entityUtils.getUserIfExists(userId);

        var itemRequest = entityUtils.getItemRequestIfExists(id);

        var items = itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return toItemRequestDto(itemRequest, items);
    }

    public List<ItemRequestDto> findAllByParams(long userId, Pageable pageable) {
        return itemRequestRepository.findAllByRequesterIdNot(userId, pageable).stream()
                .map(itemRequest -> toItemRequestDto(itemRequest,
                        getItemDtoListByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    private List<ItemDto> getItemDtoListByRequestId(long requestsId) {
        return itemRepository.findAllByRequestId(requestsId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
