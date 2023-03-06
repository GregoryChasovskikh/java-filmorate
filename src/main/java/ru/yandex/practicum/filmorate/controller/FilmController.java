package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();
    private int filmId;

    @PostMapping(value = "/films")  //добавление фильма;
    public Film addFilm(@Valid @RequestBody Film film) {
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

    @PutMapping (value = "/films") //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            System.out.println("Фильм обновлен");
            return film;
        } else {
            throw new ValidationException("There is no such film!");
        }
    }

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<Film>(films.values());
        return list;
    }

    private void validateFilm (Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration() <= 0) {
            throw new ValidationException("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive");
        }
    }

}




