package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private EntityUtils entityUtils;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestService service;

    private User user;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("ya@ya.com")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void create() {
        when(entityUtils.getUserIfExists(anyLong())).thenReturn(user);
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(ItemRequestMapper.toItemRequest(itemRequestDto));

        Assertions.assertEquals(itemRequestDto, service.create(itemRequestDto, 1L));
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void findAllByUser() {
        when(entityUtils.getUserIfExists(anyLong())).thenReturn(user);
        when(requestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(List.of(ItemRequestMapper.toItemRequest(itemRequestDto)));
        Assertions.assertEquals(1, service.getListRequestsByUser(1L).size());
        Assertions.assertArrayEquals(List.of(itemRequestDto).toArray(),
                service.getListRequestsByUser(1L).toArray());
        verify(requestRepository, times(2)).findAllByRequesterId(anyLong());
    }

    @Test
    void findById() {
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Collections.emptyList());
        when(entityUtils.getItemRequestIfExists(anyLong()))
                .thenReturn(ItemRequestMapper.toItemRequest(itemRequestDto));

        Assertions.assertEquals(itemRequestDto, service.findById(1L, 1L));
    }

    @Test
    void findAllByParams() {
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Collections.emptyList());
        when(requestRepository.findAllByRequesterIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ItemRequestMapper.toItemRequest(itemRequestDto))));

        Assertions.assertEquals(List.of(itemRequestDto), service.findAllByParams(1L, PageRequest.of(0, 2, Sort.by("created"))));
    }
}