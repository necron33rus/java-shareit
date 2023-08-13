package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto create(UserDto userDto) {
        if (findByEmail(userDto.getEmail()).isPresent()) {
            throw new AlreadyExistException("UserService: user with email '" + userDto.getEmail() + "' already exist");
        }
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    public UserDto update(UserDto userDto, long userId) {
        var updatedUser = findById(userId);
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkForDuplicateEmail(userDto.getEmail(), userId);
            updatedUser.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(updatedUser)));
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public UserDto findById(Long id) {
        return Optional.ofNullable(userStorage.findById(id))
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("UserService: user with id = " + id + "not found"));
    }

    public List<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    private void checkForDuplicateEmail(String email, long userId) {
        var otherUser = findByEmail(email).orElse(null);
        if (otherUser != null && otherUser.getEmail().equals(email) && otherUser.getId() != userId) {
            throw new AlreadyExistException("User with email=" + email + " already exists");
        }
    }
}
