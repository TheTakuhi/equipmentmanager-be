package com.interstellar.equipmentmanager.event;

import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartupLdapUsersSync {
    private final UserSyncService userSyncService;

    @Value("${spring.sql.init.mode}")
    private String isMockedDatabase;

    @Bean
    CommandLineRunner startupLdapUsersSyncCommandLineRunner() {
        return args -> {
            if (!isMockedDatabase.equals("always")) {
                userSyncService.syncAllUsersFromLdapQL();
            }
        };
    }
}
