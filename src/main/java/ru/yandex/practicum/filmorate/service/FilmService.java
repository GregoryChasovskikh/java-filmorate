package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public InMemoryFilmStorage inMemoryFilmStorage;
    public UserService userService;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, UserService userService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.userService = userService;
    }


    public Film addFilm(Film film) { //Создать новый фильм
        validateFilm(film);
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) { //Обновить уже существующий фильм
        validateFilm(film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    public List<Film> getFilmsList() { //Сгенерировать список из фильмов
        List<Film> list = new ArrayList<Film>(inMemoryFilmStorage.getFilms().values());
        return list;
    }

    public Film getFilmById(int id) { //Получить фильм по его id
        if (inMemoryFilmStorage.getFilmById(id) != null) {
            return inMemoryFilmStorage.getFilmById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such movie!");
        }
    }


    //пользователь ставит лайк фильму
    public void like(int filmId, int userId) {
        checkIfUserExists(userId);
        checkIfFilmExists(filmId);
        getFilmById(filmId).getLikes().add(userId);
    }

    //пользователь удаляет лайк
    public void unlike(int filmId, int userId) {
        checkIfUserExists(userId);
        checkIfFilmExists(filmId);
        getFilmById(filmId).getLikes().remove(userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    //Если значение параметра count не задано, верните первые 10.
    public List<Film> getMoviesByLikes(int count) {
        List<Film> sortedList = new ArrayList<>(getFilmsList());
        FilmsByLikesComparator filmsByLikesComparator = new FilmsByLikesComparator();
        sortedList.sort(filmsByLikesComparator);
        if (count == 0) {
            return sortedList.stream().limit(10).collect(Collectors.toList());
        } else {
            return sortedList.stream().limit(count).collect(Collectors.toList());
        }
    }

    private void checkIfUserExists(int id) {
        for (User currentUser : userService.getUsersList()) {
            if (id == currentUser.getId()) return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user!");
    }

    private void checkIfFilmExists(int id) {
        for (Film currentFilm : getFilmsList()) {
            if (id == currentFilm.getId()) return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such film!");
    }

    private void validateFilm (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration() <= 0) {
            throw new ValidationException("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive");
        }
    }

    private static class FilmsByLikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            if (film1.getLikes().size() > film2.getLikes().size()) {
                return -1;
            } else if (film1.getLikes().size() < film2.getLikes().size()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
