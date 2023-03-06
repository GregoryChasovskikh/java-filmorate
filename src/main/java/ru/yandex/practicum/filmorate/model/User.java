package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    public User(@NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
    }

    private int id;
    @NotNull
    @NotBlank
    @NonNull private String email;
    @NotNull
    @NotBlank
    @NonNull private String login;
    private String name;
    private LocalDate birthday;
    public Set<Integer> friends = new HashSet<>();
}
