package org.youjhin.hw10authservicetesting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.youjhin.hw10authservicetesting.models.UserEntity;
import org.youjhin.hw10authservicetesting.services.AuthServiceImpl;

/**
 * Контроллер для обработки запросов, связанных с регистрацией пользователей.
 * Этот класс отвечает за прием веб-запросов для регистрации новых пользователей,
 * делегирует обработку запросов сервису аутентификации и возвращает соответствующий ответ.
 */
@Controller
public class RegistrationController {

    private final AuthServiceImpl authService;

    /**
     * Конструктор, внедряющий зависимость сервиса аутентификации.
     * @param authService Сервис аутентификации, используемый для регистрации пользователей.
     */
    @Autowired
    public RegistrationController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    /**
     * Обрабатывает POST-запрос для регистрации нового пользователя.
     * Этот метод принимает данные пользователя из формы, пытается зарегистрировать пользователя
     * через {@link AuthServiceImpl#register(UserEntity)}. Если пользователь с таким именем уже существует,
     * добавляет сообщение об ошибке в модель и возвращает имя представления регистрации. В случае успешной
     * регистрации перенаправляет на страницу входа.
     * @param user Объект {@link UserEntity}, содержащий данные пользователя для регистрации.
     * @param model Модель для передачи данных между контроллером и представлением.
     * @return Имя представления для перенаправления или отображения.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity user, Model model) {
        boolean isRegistered = authService.register(user);
        if (!isRegistered) {
            // Пользователь с таким именем уже существует, добавляем сообщение об ошибке в модель
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register"; // Возвращаемся на страницу регистрации с сообщением об ошибке
        }
        // Регистрация прошла успешно, перенаправляем на страницу входа
        return "redirect:/login";
    }
}
