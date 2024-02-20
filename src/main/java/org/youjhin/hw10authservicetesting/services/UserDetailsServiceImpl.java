package org.youjhin.hw10authservicetesting.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.youjhin.hw10authservicetesting.models.UserEntity;
import org.youjhin.hw10authservicetesting.repositorys.UserRepository;

import java.util.ArrayList;

/**
 * Сервис для работы с деталями пользователя, реализующий интерфейс UserDetailsService.
 * Данный класс используется Spring Security для загрузки данных пользователя по имени пользователя.
 * Он обращается к {@link UserRepository} для получения сущности пользователя и преобразует ее в {@link UserDetails}.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор класса с внедрением зависимости репозитория пользователей.
     *
     * @param userRepository Репозиторий для доступа к данным пользователей.
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по его имени пользователя.
     * Использует {@link UserRepository} для поиска пользователя по имени пользователя. Если пользователь не найден,
     * выбрасывается исключение {@link UsernameNotFoundException}. В противном случае возвращает {@link UserDetails},
     * представляющий найденного пользователя.
     * @param username Имя пользователя, по которому происходит поиск.
     * @return Детали пользователя, представленные через {@link UserDetails}.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }
}
