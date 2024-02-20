package org.youjhin.hw10authservicetesting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.youjhin.hw10authservicetesting.models.UserEntity;
import org.youjhin.hw10authservicetesting.repositorys.UserRepository;
import org.youjhin.hw10authservicetesting.services.AuthServiceImpl;

import java.util.Optional;

/**
 * Контроллер для обработки запросов на вход и выход пользователей.
 * Этот класс отвечает за обработку запросов на аутентификацию пользователя (вход в систему)
 * и завершение сеанса пользователя (выход из системы). Он делегирует выполнение этих операций
 * сервису аутентификации и возвращает соответствующие представления или перенаправления.
 */
@Controller
public class LogInAndOutController {

    private final AuthServiceImpl authService;
    private final UserRepository userRepository;
    private Long userId; // Идентификатор текущего аутентифицированного пользователя

    /**
     * Конструктор для внедрения зависимостей сервиса аутентификации и репозитория пользователей.
     * @param authService Сервис аутентификации для выполнения входа и выхода.
     * @param userRepository Репозиторий пользователей для поиска пользователя по имени.
     */
    @Autowired
    public LogInAndOutController(AuthServiceImpl authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * Обрабатывает POST-запрос на вход пользователя в систему.
     * Проверяет учетные данные пользователя и в случае успешной аутентификации перенаправляет
     * на главную страницу. В случае неудачи перенаправляет на страницу с ошибкой.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return Имя представления или перенаправление.
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        user.ifPresent(userEntity -> userId = userEntity.getId()); // получаем Id текущего пользователя
        boolean login = authService.login(username, password);
        if (login) {
            return "home"; // Перенаправление на главную страницу в случае успешного входа
        }
        return "redirect:/errorpage"; // Перенаправление на страницу ошибки в случае неудачи
    }

    /**
     * Обрабатывает GET-запрос на выход пользователя из системы.
     * Выполняет выход текущего пользователя, очищая его сессию, и перенаправляет на страницу входа.
     * @return Перенаправление на страницу входа.
     */
    @GetMapping("/exit")
    public String logout() {
        authService.logout(userId); // Выполнение выхода для текущего пользователя
        return "redirect:/login"; // Перенаправление на страницу входа
    }
}


