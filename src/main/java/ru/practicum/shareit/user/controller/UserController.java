package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("UserController: POST method: create item {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        log.info("UserController: GET method: request user with id = {}", id);
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("UserController: GET method: request all users");
        return userService.findAll();
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("UserController: PATH method: request PATCH /users/{} by user = {}", id, userDto);
        return userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("UserController: DELETE method: request deleting user with id = {}", id);
        userService.deleteById(id);
    }
}
