package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
@Service
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();

    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
