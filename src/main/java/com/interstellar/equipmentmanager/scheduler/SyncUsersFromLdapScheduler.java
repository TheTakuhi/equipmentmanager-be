package com.interstellar.equipmentmanager.scheduler;

import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncUsersFromLdapScheduler {
    private final UserSyncService userSyncService;

    @Scheduled(cron = "${scheduler.syncUsersFromLdap.cron}", zone = "${scheduler.timezone}")
    public void syncUsersFromLdap() {
        System.out.println("Syncing all users from LDAPQL");
        userSyncService.syncAllUsersFromLdapQL();
    }
}
