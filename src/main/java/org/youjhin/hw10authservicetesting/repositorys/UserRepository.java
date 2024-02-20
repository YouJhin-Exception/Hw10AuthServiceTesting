package org.youjhin.hw10authservicetesting.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youjhin.hw10authservicetesting.models.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

}
