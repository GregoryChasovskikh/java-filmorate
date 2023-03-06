package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private int userId;

    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
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

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            System.out.println("Пользователь успешно обновлен!");
            return user;
        } else {
            throw new ValidationException("There is no such user!");
        }

    }


    public List<User> getUsersList() {
        List<User> list = new ArrayList<User>(users.values());
        return list;
    }


    private void validateUser (User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future");
        } else if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public User getUserById(int id) {
        for (User currentUser : users.values()) {
            if (id == currentUser.getId()) return currentUser;
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "There is no such user!");
    }

}
