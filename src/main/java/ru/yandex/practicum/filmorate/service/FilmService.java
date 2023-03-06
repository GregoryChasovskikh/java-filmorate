package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    //пользователь ставит лайк фильму
    public void like(int filmId, int userId) {
        inMemoryFilmStorage.getFilmById(filmId).getWhoLiked().add(userId);
    }

    //пользователь удаляет лайк
    public void unlike(int filmId, int userId) {
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

    private static class FilmsByLikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            if (film1.getWhoLiked().size() > film2.getWhoLiked().size()) {
                return 1;
            } else if (film1.getWhoLiked().size() < film2.getWhoLiked().size()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
