package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Set;
@Validated
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("/users")
    public Set<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        User created = userService.create(user);
        return created;
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) throws UserNotFoundException {
        User updated = userService.update(user);
        return updated;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable Long id, @PathVariable Long friendId) throws UserNotFoundException {
        friendService.addFriendToUser(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable Long id,
                              @PathVariable Long friendId) throws UserNotFoundException {
        friendService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getListOfCommonFriends(@PathVariable Long id,
                                            @PathVariable Long otherId) throws UserNotFoundException {
        return friendService.getCommonFriend(id, otherId);
    }

    @GetMapping("users/{id}/friends")
    public Set<User> getFriendsOfUser(@PathVariable Long id) throws UserNotFoundException {
        return friendService.getFriendsOfUser(id);
    }
}
