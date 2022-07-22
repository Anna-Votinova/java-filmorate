package ru.yandex.practicum.filmorate.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
public class UserController {

    private final InMemoryUserStorage storage;
    private final UserService userService;

    public UserController(InMemoryUserStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Set<User> findAll() {
        return storage.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        return storage.findUserById(id);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return storage.create(user);
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws UserNotFoundException {
        return storage.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToSetOfFriends(@PathVariable Long id, @PathVariable Long friendId) throws UserNotFoundException {
        userService.addToSetOfFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public String deleteUserByIdFromSetOfFriends (@PathVariable Long id,
                                                  @PathVariable Long friendId) throws UserNotFoundException {
        return userService.deleteUserByIdFromSetOfFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getListOfCommonFriends(@PathVariable Long id,
                                            @PathVariable Long otherId) throws UserNotFoundException {
        return userService.getListOfCommonFriends(id, otherId)
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toSet());
    }

    @GetMapping("users/{id}/friends")
    public Set<User> getFriendsOfUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getFriendsOfUser(id)
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toSet());
    }
}
