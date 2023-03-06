package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;
    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping(value = "/users") //Создание пользователя
    public User addUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping(value = "/users") //Обновление пользователя
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);

    }

    @GetMapping("/users") //Получение списка пользователей
    public List<User> getUsersList() {
        return inMemoryUserStorage.getUsersList();
    }

    @GetMapping(value = "/users/{id}") //получение по id http://localhost:8080/users/1
    public User getUserById(@PathVariable int id) {
        return inMemoryUserStorage.getUserById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}") //добавление в друзья
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")//удаление из друзей
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends") //возвращаем список пользователей, являющихся его друзьями
    public List<User> getListOfFriends(@PathVariable int id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getListOfMutualFriends(id, otherId);
    }

}
