package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Service
public class InMemoryFilmStorage implements FilmStorage { //add, remove, getById, update
    private HashMap<Integer, Film> films = new HashMap<>();
    private int filmId;

    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            ++filmId;
            film.setId(filmId);
            films.put(filmId, film);
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This movie is already in the database!");
        }
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no such film!");
        }
    }

    public Film getFilmById(int id) {
        for (Film currentFilm : films.values()) {
            if (id == currentFilm.getId()) return currentFilm;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such movie!");
    }

    public void deleteFilm(int id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no such film!");
        }
    }

}
