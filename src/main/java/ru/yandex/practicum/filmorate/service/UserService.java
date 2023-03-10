package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    public InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    private int userId;

    public User addUser(User user) { //Добавление нового пользователя
        validateUser(user);
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) { //Изменение данных о пользователе
        validateUser(user);
        return inMemoryUserStorage.updateUser(user);
    }


    public List<User> getUsersList() { //писок всех пользователей
        List<User> list = new ArrayList<User>(inMemoryUserStorage.getUsers().values());
        return list;
    }

    public User getUserById(int id) { //Получение пользователя по id
        if (inMemoryUserStorage.getUserById(id) != null) {
            return inMemoryUserStorage.getUserById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user!");
        }
    }

    //Добавление в друзья
    public void addFriend(int idUserOne, int idUserTwo) {
        checkIfFriendExists(idUserOne);
        checkIfFriendExists(idUserTwo);
        getUserById(idUserOne).getFriends().add(idUserTwo);
        getUserById(idUserTwo).getFriends().add(idUserOne);
    }

    //Удаление из друзей
    public void deleteFriend(int idUserOne, int idUserTwo) {
        checkIfFriendExists(idUserOne);
        checkIfFriendExists(idUserTwo);
        getUserById(idUserOne).getFriends().remove(idUserTwo);
        getUserById(idUserTwo).getFriends().remove(idUserOne);
    }
    //список пользователей, являющихся его друзьями
    public List<User> getListOfFriends(int idUserOne) {
        return setToList(getUserById(idUserOne).getFriends());
    }

    //список друзей, общих с другим пользователем
    public List<User> getListOfMutualFriends(int idUserOne, int idUserTwo) {
        Set<Integer> userOneFriends = new HashSet<>(getUserById(idUserOne).getFriends());
        Set<Integer> userTwoFriends = new HashSet<>(getUserById(idUserTwo).getFriends());
        userOneFriends.removeAll(getUserById(idUserTwo).getFriends());
        userTwoFriends.removeAll(getUserById(idUserOne).getFriends());
        Set<Integer> nonMutualFriends = mergeSet(userOneFriends, userTwoFriends);
        Set<Integer> mutualFriends = mergeSet(getUserById(idUserOne).getFriends(), getUserById(idUserTwo).getFriends());
        mutualFriends.removeAll(nonMutualFriends);
        return setToList(mutualFriends);
    }

    private List<User> setToList(Set<Integer> friendsFromSet) {
        List<User> friends = new ArrayList<>();
        for (Integer friend : friendsFromSet) {
            friends.add(getUserById(friend));
        }
        return friends;
    }

    private void checkIfFriendExists(int id) {
        for (User currentUser : getUsersList()) {
            if (id == currentUser.getId()) return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user!");
    }

    private void validateUser (User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future");
        } else if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }

    private <T> Set<T> mergeSet(Set<T> a, Set<T> b)
    {
        return new HashSet<T>() {
            {
                addAll(a);
                addAll(b);
            }
        };
    }

}
