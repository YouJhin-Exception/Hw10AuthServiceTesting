package org.youjhin.hw10authservicetesting.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.youjhin.hw10authservicetesting.models.SessionEntity;
import org.youjhin.hw10authservicetesting.models.UserEntity;
import org.youjhin.hw10authservicetesting.repositorys.SessionRepository;
import org.youjhin.hw10authservicetesting.repositorys.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        // Инициализация тестового пользователя
        user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setId(1L);

        // Настройка mock для кодировщика паролей для возврата зашифрованного пароля
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void whenRegisterUser_thenSaveUser() {
        // Настройка поведения: при поиске пользователя возвращается пустой Optional
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Выполнение действия: попытка регистрации пользователя
        boolean result = authService.register(user);

        // Проверка результата: пользователь успешно зарегистрирован
        assertTrue(result);
        // Проверка вызова: метод save репозитория пользователя должен быть вызван один раз
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void whenLoginExistingUser_thenCreateSession() {
        // Настройка поведения: при поиске пользователя возвращается существующий пользователь
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        // Настройка поведения: пароль совпадает
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Выполнение действия: попытка входа в систему
        boolean loginResult = authService.login("testUser", "testPassword");

        // Проверка результата: вход выполнен успешно
        assertTrue(loginResult);
        // Проверка вызова: метод save репозитория сессий должен быть вызван один раз для создания новой сессии
        verify(sessionRepository, times(1)).save(any(SessionEntity.class));
    }


    @Test
    void whenLogout_thenDeleteSessionAndClearSecurityContext() {
        // Настройка поведения: при удалении сессии никаких дополнительных действий не происходит
        doNothing().when(sessionRepository).deleteByUserId(anyLong());

        // Выполнение действия: выход из системы
        authService.logout(1L);

        // Проверка вызова: метод deleteByUserId репозитория сессий должен быть вызван один раз
        verify(sessionRepository, times(1)).deleteByUserId(anyLong());
        // Проверка: контекст безопасности должен быть очищен (в тестовой среде может потребоваться дополнительная настройка)
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
