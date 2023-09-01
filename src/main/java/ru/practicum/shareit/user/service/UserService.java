package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.EntityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EntityUtils entityUtils;

    @Transactional
    public UserDto create(@NotNull UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public UserDto update(@NotNull UserDto userDto, long userId) {
        var updatedUser = entityUtils.getUserIfExists(userId);
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkForDuplicateEmail(userDto.getEmail(), userId);
            updatedUser.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserService: user with id = " + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkForDuplicateEmail(String email, long userId) {
        var otherUser = userRepository.findByEmail(email)
                .map(UserMapper::toUserDto)
                .orElse(null);
        if (otherUser != null && otherUser.getEmail().equals(email) && otherUser.getId() != userId) {
            throw new AlreadyExistException("User with email=" + email + " already exists");
        }
    }
}
