package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.entity.LoginHistoryEntity;
import com.enus.newsletter.db.repository.imp.ILoginHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@Transactional
public class LoginHistoryRepository {
    private final ILoginHistoryRepository loginHistoryRepository;

    public LoginHistoryRepository(ILoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    public void saveLoginHistory(String username, String ipAddress, int status, String reason) {
        // create a record in the login history table
        LoginHistoryEntity loginHistory = LoginHistoryEntity
                .builder()
                .username(username)
                .ipAddress(ipAddress)
                .loginStatus(status)
                .reason(reason)
                .build();

        loginHistoryRepository.save(loginHistory);
    }
}
