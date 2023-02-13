package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    public Film(@NonNull String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @NonNull private int id;
    @NonNull private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
