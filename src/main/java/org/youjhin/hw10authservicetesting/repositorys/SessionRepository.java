package org.youjhin.hw10authservicetesting.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youjhin.hw10authservicetesting.models.SessionEntity;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    void deleteByUserId(Long userId);

}
