package org.youjhin.hw10authservicetesting.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youjhin.hw10authservicetesting.models.SessionEntity;
import org.youjhin.hw10authservicetesting.models.UserEntity;
import org.youjhin.hw10authservicetesting.repositorys.SessionRepository;
import org.youjhin.hw10authservicetesting.repositorys.UserRepository;
import org.youjhin.hw10authservicetesting.services.interfaces.AuthService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Класс сервиса для операций аутентификации.
 * Этот класс предоставляет функциональность для регистрации пользователей, входа в систему и выхода из неё.
 * Он взаимодействует с {@link UserRepository} и {@link SessionRepository} для управления данными пользователей
 * и данными сессий соответственно. Для шифрования паролей используется {@link PasswordEncoder}.
 *
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор AuthServiceImpl с необходимыми репозиториями и кодировщиком.
     *
     * @param userRepository Репозиторий для доступа к данным пользователя.
     * @param sessionRepository Репозиторий для управления данными сессий.
     * @param passwordEncoder Кодировщик для шифрования паролей.
     */
    public AuthServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя в системе.
     * Проверяет, существует ли пользователь с таким же именем. Если нет, шифрует пароль и сохраняет пользователя.
     * @param user Сущность пользователя, которого нужно зарегистрировать.
     * @return true, если регистрация успешна, иначе false.
     */
    public boolean register(UserEntity user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Пользователь с таким именем уже существует
            return false;
        }
        // Шифрование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        // После регистрации можно создать сессию, если это требуется
        return true;
    }

    /**
     * Осуществляет вход пользователя в систему.
     * Проверяет наличие пользователя с таким именем и соответствие пароля. В случае успеха создает сессию.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return true, если вход успешен, иначе false.
     */
    public boolean login(String username, String password) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setUserId(user.get().getId());
            sessionEntity.setCreatedAt(LocalDateTime.now());
            sessionRepository.save(sessionEntity);
            return true;
        }
        return false;
    }

    /**
     * Выполняет выход пользователя из системы.
     * Удаляет сессию пользователя и очищает контекст безопасности.
     * @param userId Идентификатор пользователя, который должен быть выведен из системы.
     */
    @Transactional
    public void logout(Long userId) {
        sessionRepository.deleteByUserId(userId);
        SecurityContextHolder.clearContext();
    }
}

