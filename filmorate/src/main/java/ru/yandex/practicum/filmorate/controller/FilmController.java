package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();
    private int filmId;

    @PostMapping(value = "/films")  //добавление фильма;
    public Film addFilm(@RequestBody Film film) {
        if (checkIfContainsName(film.getName()) == 0) {
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

    @PutMapping (value = "/films") //обновление фильма;
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        Integer currentFilmId = checkIfContainsName(film.getName());
        if (currentFilmId > 0) {
            films.remove(currentFilmId);
            film.setId(currentFilmId);
            films.put(currentFilmId, film);
            System.out.println("Фильм обновлен");
        } else {
            throw new ValidationException("Фильма нет в базе. Попробуйте его добавить");
        }
        return film;
    }

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<Film>(films.values());
        return list;
    }

    private void validateFilm (Film film) {
        if (film.getName().equals("") || film.getDescription().length() > 200 || film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive");
        }
    }

    private Integer checkIfContainsName (String name) {
        for (Film film : films.values()) {
            if (film.getName().equals(name)) {
                return film.getId();
            }
        }
        return 0;
    }

}




