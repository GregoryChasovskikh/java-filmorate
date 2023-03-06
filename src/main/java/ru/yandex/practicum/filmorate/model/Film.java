package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    public Film(@NotBlank @NonNull String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film() {
    }

    @NotNull
    @NonNull private int id;
    @NotNull
    @NonNull
    @NotBlank private String name;
    @Size(max=200) private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Set<Integer> whoLiked = new HashSet<>();
}
