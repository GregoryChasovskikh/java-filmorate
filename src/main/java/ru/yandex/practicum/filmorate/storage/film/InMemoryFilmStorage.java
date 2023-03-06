package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int filmId;
    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            validateFilm(film);
            ++filmId;
            film.setId(filmId);
            films.put(filmId, film);
            System.out.println("Фильм добавлен");
        } else {
            throw new ValidationException("Такой фильм уже есть в базе!");
        }
        return film;
    }


    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            System.out.println("Фильм обновлен");
            return film;
        } else {
            throw new ValidationException("There is no such film!");
        }
    }


    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<Film>(films.values());
        return list;
    }


    private void validateFilm (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration() <= 0) {
            throw new ValidationException("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive");
        }
    }

    public Film getFilmById(int id) {
        for (Film currentFilm : films.values()) {
            if (id == currentFilm.getId()) return currentFilm;
        }
        throw new ValidationException("There is no such movie!");
    }
}
