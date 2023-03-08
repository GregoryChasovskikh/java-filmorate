package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;

@Service
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    public HashMap<Integer, Film> getFilms() {
        return films;
    }
}
