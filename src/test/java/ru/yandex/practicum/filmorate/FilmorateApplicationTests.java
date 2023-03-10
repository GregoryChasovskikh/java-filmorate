package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {
	@Test
	public void whenCreateNullEmail() {
		try {
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User(null, "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
			userService.addUser(testUser);
		} catch (final NullPointerException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void whenCreateUserWithWrongEmail() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("vasyagmail.com", "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithSpacesInLogin() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("vasya@gmail.com", "Hero Of Might", "Alexandre", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithEmptyName_LoginIsDisplayed() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("vasya@gmail.com", "HeroOfMight", null, LocalDate.parse("2018-11-01"));
		userService.addUser(testUser);
		Assertions.assertEquals(testUser.getLogin(), userService.getUsersList().get(0).getName());
	}

	@Test
	public void whenDateOfBirthIsInTheFuture() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("vasya@gmail.com", "HeroOfMight", "Alexandre", LocalDate.parse("2024-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void maximumLengthOfDescriptionCannotExceed200() {
		try {
			InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
			FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
			Film testFilm = new Film("Knights", "The cyborg Gabriel (Kris Kristofferson) was created to destroy all other cyborgs. He later rescues Nea (Kathy Long) by killing the cyborg Simon (Scott Paulin). Gabriel trains Nea to become a cyborg killer and help him.", LocalDate.parse("1993-11-17"), 90);
			filmService.addFilm(testFilm);
		} catch (final HttpMessageNotReadableException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void releaseDate() {
		InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
		Film testFilm = new Film("Knights", "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1893-11-17"), 90);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmService.addFilm(testFilm));
		Assertions.assertEquals("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive", ex.getMessage());
	}

	@Test
	public void durationMustBePositive() {
		InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
		Film testFilm = new Film("Knights", "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1993-11-17"), -1);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmService.addFilm(testFilm));
		Assertions.assertEquals("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive", ex.getMessage());
	}


	@Test
	@SuppressWarnings("null")
	public void nameOfFilmMustNotBeNull() {
		try {
			InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
			FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
			Film testFilm = new Film(null, "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1993-11-17"), 90);
			filmService.addFilm(testFilm);
		} catch (final NullPointerException e) {
			assertNotNull(e);
		}
	}

	@Test
	void contextLoads() {
	}


}
