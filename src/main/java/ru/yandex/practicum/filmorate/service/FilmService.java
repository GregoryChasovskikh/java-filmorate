package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public InMemoryFilmStorage inMemoryFilmStorage;
    public InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    //пользователь ставит лайк фильму
    public void like(int filmId, int userId) {
        checkIfUserExists(userId);
        checkIfFilmExists(filmId);
        inMemoryFilmStorage.getFilmById(filmId).getWhoLiked().add(userId);
    }

    //пользователь удаляет лайк
    public void unlike(int filmId, int userId) {
        checkIfUserExists(userId);
        checkIfFilmExists(filmId);
        inMemoryFilmStorage.getFilmById(filmId).getWhoLiked().remove(userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    //Если значение параметра count не задано, верните первые 10.
    public List<Film> getMoviesByLikes(int count) {
        List<Film> sortedList = new ArrayList<>(inMemoryFilmStorage.getFilmsList());
        FilmsByLikesComparator filmsByLikesComparator = new FilmsByLikesComparator();
        sortedList.sort(filmsByLikesComparator);
        if (count == 0) {
            return sortedList.stream().limit(10).collect(Collectors.toList());
        } else {
            return sortedList.stream().limit(count).collect(Collectors.toList());
        }
    }

    private void checkIfUserExists(int id) {
        for (User currentUser : inMemoryUserStorage.getUsersList()) {
            if (id == currentUser.getId()) return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user!");
    }

    private void checkIfFilmExists(int id) {
        for (Film currentFilm : inMemoryFilmStorage.getFilmsList()) {
            if (id == currentFilm.getId()) return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such film!");
    }

    private static class FilmsByLikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            if (film1.getWhoLiked().size() > film2.getWhoLiked().size()) {
                return -1;
            } else if (film1.getWhoLiked().size() < film2.getWhoLiked().size()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
