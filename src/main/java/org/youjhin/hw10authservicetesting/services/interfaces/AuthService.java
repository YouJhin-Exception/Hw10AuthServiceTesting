package org.youjhin.hw10authservicetesting.services.interfaces;

import org.youjhin.hw10authservicetesting.models.UserEntity;

public interface AuthService {

    boolean register(UserEntity user);

    boolean login(String username, String password);

    void logout(Long userId);


}
