package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User findById(Long id);

    User update(User user);

    void deleteById(Long id);

    List<User> findAll();

}
