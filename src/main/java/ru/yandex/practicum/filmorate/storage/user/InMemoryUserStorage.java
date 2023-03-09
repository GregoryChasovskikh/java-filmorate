package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
@Service
public class InMemoryUserStorage implements UserStorage { //add, remove, getById, update
    private HashMap<Integer, User> users = new HashMap<>();

    public HashMap<Integer, User> getUsers() {
        return users;
    }
    private int userId;

    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            ++userId;
            user.setId(userId);
            users.put(userId, user);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This user is already in the database!");
        }
        return user;
    }

    public User updateUser(User user) { //Изменение данных о пользователе
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no such user!");
        }
    }

    public User getUserById(int id) { //Получение пользователя по id
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user!");
        }
    }

    public void deleteUser(int id) {
            users.remove(id);
    }
}
