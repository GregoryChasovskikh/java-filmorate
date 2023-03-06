package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    public InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //Добавление в друзья
    public void addFriend(int idUserOne, int idUserTwo) {
        inMemoryUserStorage.getUserById(idUserOne).getFriends().add(idUserTwo);
        inMemoryUserStorage.getUserById(idUserTwo).getFriends().add(idUserOne);
    }

    //Удаление из друзей
    public void deleteFriend(int idUserOne, int idUserTwo) {
        inMemoryUserStorage.getUserById(idUserOne).getFriends().remove(idUserTwo);
        inMemoryUserStorage.getUserById(idUserTwo).getFriends().remove(idUserOne);
    }
    //список пользователей, являющихся его друзьями
    public List<User> getListOfFriends(int idUserOne) {
        return setToList(inMemoryUserStorage.getUserById(idUserOne).getFriends());
    }

    //список друзей, общих с другим пользователем
    public List<User> getListOfMutualFriends(int idUserOne, int idUserTwo) {
        Set<Integer> userOneFriends = new HashSet<>(inMemoryUserStorage.getUserById(idUserOne).getFriends());
        Set<Integer> userTwoFriends = new HashSet<>(inMemoryUserStorage.getUserById(idUserTwo).getFriends());
        userOneFriends.removeAll(inMemoryUserStorage.getUserById(idUserTwo).getFriends());
        userTwoFriends.removeAll(inMemoryUserStorage.getUserById(idUserOne).getFriends());
        Set<Integer> nonMutualFriends = mergeSet(userOneFriends, userTwoFriends);
        Set<Integer> mutualFriends = mergeSet(inMemoryUserStorage.getUserById(idUserOne).getFriends(), inMemoryUserStorage.getUserById(idUserTwo).getFriends());
        mutualFriends.removeAll(nonMutualFriends);
        return setToList(mutualFriends);
    }

    private List<User> setToList(Set<Integer> friendsFromSet) {
        List<User> friends = new ArrayList<>();
        for (Integer friend : friendsFromSet) {
            friends.add(inMemoryUserStorage.getUserById(friend));
        }
        return friends;
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
