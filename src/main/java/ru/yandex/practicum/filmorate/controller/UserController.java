package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping(value = "/users") //Создание пользователя
    public void addUser(@RequestBody User user) {
        if (checkIfContainsLogin(user.getLogin()) == 0) {
            validateUser(user);
            ++userId;
            user.setId(userId);
            users.put(userId, user);
            System.out.println("Пользователь добавлен");
        } else {
            throw new ValidationException("This user is already in the database!");
        }

    }

    @PutMapping(value = "/users/update") //Обновление пользователя
    public void updateUser(@RequestBody User user) {
        Integer currentUserId = checkIfContainsLogin(user.getLogin());
        if (currentUserId > 0) {
            validateUser(user);
            users.remove(currentUserId);
            user.setId(currentUserId);
            users.put(currentUserId, user);
            System.out.println("Пользователь успешно обновлен!");
        } else {
            throw new ValidationException("This user is not in the database!");
        }


    }

    @GetMapping("/users") //Получение списка пользователей
    public HashMap<Integer, User> getUsersList() {
        return users;
    }

    private void validateUser (User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    private Integer checkIfContainsLogin (String login) {
        for (User user : users.values()) {
            if (user.getLogin().equals(login)) {
                return user.getId();
            }
        }
        return 0;
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
