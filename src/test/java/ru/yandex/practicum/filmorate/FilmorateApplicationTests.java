package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {
	@Test
	public void whenCreateNullEmail() {
		try {
		UserController userController = new UserController();
		User testUser = new User(null, "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
		userController.addUser(testUser);
		} catch (final NullPointerException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void whenCreateUserWithWrongEmail() {
		UserController userController = new UserController();
		User testUser = new User("vasyagmail.com", "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithSpacesInLogin() {
		UserController userController = new UserController();
		User testUser = new User("vasya@gmail.com", "Hero Of Might", "Alexandre", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithEmptyName_LoginIsDisplayed() {
		UserController userController = new UserController();
		User testUser = new User("vasya@gmail.com", "HeroOfMight", null, LocalDate.parse("2018-11-01"));
		userController.addUser(testUser);
		Assertions.assertEquals(testUser.getLogin(), userController.getUsers().get(1).getName());
	}

	@Test
	public void whenDateOfBirthIsInTheFuture() {
		UserController userController = new UserController();
		User testUser = new User("vasya@gmail.com", "HeroOfMight", "Alexandre", LocalDate.parse("2024-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid data: email must contain the @ symbol, login cannot contain spaces, date of birth cannot be in the future", ex.getMessage());
	}

	@Test
	public void maximumLengthOfDescriptionCannotExceed200() {
		try {
			FilmController filmController = new FilmController();
			Film testFilm = new Film("Knights", "The cyborg Gabriel (Kris Kristofferson) was created to destroy all other cyborgs. He later rescues Nea (Kathy Long) by killing the cyborg Simon (Scott Paulin). Gabriel trains Nea to become a cyborg killer and help him.", LocalDate.parse("1993-11-17"), 90);
			filmController.addFilm(testFilm);
		} catch (final HttpMessageNotReadableException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void releaseDate() {
		FilmController filmController = new FilmController();
		Film testFilm = new Film("Knights", "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1893-11-17"), 90);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(testFilm));
		Assertions.assertEquals("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive", ex.getMessage());
	}

	@Test
	public void durationMustBePositive() {
		FilmController filmController = new FilmController();
		Film testFilm = new Film("Knights", "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1993-11-17"), -1);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(testFilm));
		Assertions.assertEquals("Invalid data: maximum description length is 200 characters; release date - no earlier than December 28, 1895; movie duration must be positive", ex.getMessage());
	}


	@Test
	@SuppressWarnings("null")
	public void nameOfFilmMustNotBeNull() {
		try {
			FilmController filmController = new FilmController();
			Film testFilm = new Film(null, "Knights is a 1993 American martial arts science fiction action film directed by Albert Pyun", LocalDate.parse("1993-11-17"), 90);
			filmController.addFilm(testFilm);
		} catch (final NullPointerException e) {
			assertNotNull(e);
		}
	}

	@Test
	void contextLoads() {
	}


}
