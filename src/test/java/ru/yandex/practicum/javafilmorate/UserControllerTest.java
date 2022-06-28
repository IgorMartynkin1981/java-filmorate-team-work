package ru.yandex.practicum.javafilmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.javafilmorate.controller.UserController;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends UserController {

    protected User user = new User();

    @BeforeEach
    void beforeEach() {
        user.setEmail("Email@email");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1995, 10, 20));
    }

    @Test
    void expectExceptionEmptyEmailByCreateUser() {
        //Подготовка
        user.setEmail("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        createUser(user);
                    }
                });

        assertEquals("Адрес электронной почты не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionEmptyLoginByCreateUser() {
        //Подготовка
        user.setLogin("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        createUser(user);
                    }
                });

        assertEquals("Логин не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionBirthDayAfterNowByCreateUser() {
        //Подготовка
        user.setBirthday(LocalDate.of(2040, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        createUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть больше " + LocalDate.now(), ex.getMessage());
    }

    @Test
    void expectExceptionEmptyEmailByUpdateUser() {
        //Подготовка
        user.setEmail("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        updateUser(user);
                    }
                });

        assertEquals("Адрес электронной почты не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionEmptyLoginByUpdateUser() {
        //Подготовка
        user.setLogin("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        updateUser(user);
                    }
                });

        assertEquals("Логин не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionBirthDayAfterNowByUpdateUser() {
        //Подготовка
        user.setBirthday(LocalDate.of(2040, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        updateUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть больше " + LocalDate.now(), ex.getMessage());
    }
}
