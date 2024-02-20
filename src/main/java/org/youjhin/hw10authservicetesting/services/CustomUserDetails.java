package org.youjhin.hw10authservicetesting.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.youjhin.hw10authservicetesting.models.UserEntity;

import java.util.ArrayList;

/**
 * Реализация {@link UserDetails}, предоставляющая детали аутентифицированного пользователя.
 * Этот класс служит для хранения информации о пользователе, которая необходима Spring Security
 * для аутентификации и авторизации. Класс расширяет стандартную реализацию {@link User} от Spring Security,
 * добавляя идентификатор пользователя из базы данных.
 */
@Getter
@Setter
public class CustomUserDetails extends User implements UserDetails {

    private Long userId; // Идентификатор пользователя в базе данных

    /**
     * Конструктор, инициализирующий объект {@link CustomUserDetails}.
     * Создает новый экземпляр, используя информацию из переданного объекта {@link UserEntity}.
     * Инициализирует базовый класс с именем пользователя, паролем и пустым списком ролей.
     * Также устанавливает идентификатор пользователя.
     * @param user Объект {@link UserEntity}, содержащий информацию о пользователе.
     */
    public CustomUserDetails(UserEntity user) {
        super(user.getUsername(), user.getPassword(), new ArrayList<>());
        this.userId = user.getId(); // Здесь userId уже устанавливается
    }
}
