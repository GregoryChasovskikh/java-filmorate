package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping(value = "/users") //Создание пользователя
    public User addUser(@RequestBody User user) {
        if (checkIfContainsLogin(user.getLogin()) == 0) {
            validateUser(user);
            ++userId;
            user.setId(userId);
            users.put(userId, user);
            System.out.println("Пользователь добавлен");
        } else {
            throw new ValidationException("This user is already in the database!");
        }
        return user;
    }

    @PutMapping(value = "/users") //Обновление пользователя
    public User updateUser(@RequestBody User user) {
        //Integer currentUserId = checkIfContainsLogin(user.getLogin());
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.remove(user.getId());
            users.put(user.getId(), user);
            System.out.println("Пользователь успешно обновлен!");
            return user;
        } else {
            throw new ValidationException("There is no such user!");
    }

    }

    @GetMapping("/users") //Получение списка пользователей
    public List<User> getUsersList() {
        List<User> list = new ArrayList<User>(users.values());
        return list;
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
