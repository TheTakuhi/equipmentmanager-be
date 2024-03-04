package com.interstellar.equipmentmanager.scheduler;

import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncUsersFromLdapScheduler {
    private final UserSyncService userSyncService;

    @Scheduled(cron = "${scheduler.syncUsersFromLdap.cron}", zone = "${scheduler.timezone}")
    public void syncUsersFromLdap() {
        log.info("Syncing all users from LdapQL");
        userSyncService.syncAllUsersFromLdapQL();
    }
}
