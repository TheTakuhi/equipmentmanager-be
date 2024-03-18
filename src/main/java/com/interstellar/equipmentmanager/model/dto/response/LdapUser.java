package com.interstellar.equipmentmanager.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class LdapUser {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String login;
    private LdapUser manager;
}
