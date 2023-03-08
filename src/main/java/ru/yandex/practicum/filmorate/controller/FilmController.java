package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping(value = "/films")  //добавление фильма;
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping (value = "/films") //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> getFilmsList() {
        return filmService.getFilmsList();
    }

    @GetMapping("/films/{filmId}")
    public Film findFilmById(@PathVariable int filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping (value = "/films/{id}/like/{userId}") //Пользователь ставит лайк фильму
    public void like(@PathVariable int id, @PathVariable int userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping (value = "/films/{id}/like/{userId}") //пользователь удаляет лайк
    public void unlike(@PathVariable int id, @PathVariable int userId) {
        filmService.unlike(id, userId);
    }

    @GetMapping (value = "/films/popular")
    public List<Film> getMoviesByLikes(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return filmService.getMoviesByLikes(10);
        } else {
            return filmService.getMoviesByLikes(count);
        }
    }

}




