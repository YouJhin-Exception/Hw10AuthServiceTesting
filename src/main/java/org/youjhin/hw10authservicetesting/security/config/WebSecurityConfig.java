package org.youjhin.hw10authservicetesting.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности веб-приложения.
 * Этот класс настраивает настройки безопасности для веб-приложения, включая конфигурацию цепочки фильтров безопасности
 * и метод шифрования паролей. Он включает в себя конфигурацию, позволяющую выполнить аутентификацию и авторизацию
 * на основе URL-адресов.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    /**
     * Конфигурирует цепочку фильтров безопасности для обработки запросов HTTP.
     * Определяет правила авторизации для различных эндпоинтов. Настраивает CSRF защиту, доступ к определенным страницам
     * без аутентификации и требование аутентификации для остальных запросов.
     * @param httpSecurity настройки безопасности HTTP.
     * @return сконфигурированная цепочка фильтров безопасности.
     * @throws Exception если произошла ошибка во время конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/login", "/register", "/errorpage", "/exit").permitAll()
                                .requestMatchers("/home").authenticated()
                                .anyRequest().authenticated()
                );
        return httpSecurity.build();
    }

    /**
     * Предоставляет кодировщик паролей для использования в приложении.
     * Использует {@link BCryptPasswordEncoder} для шифрования и проверки паролей.
     * @return экземпляр {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
