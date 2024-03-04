package com.interstellar.equipmentmanager.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class LdapUser {
    private UUID objectGUID;
    private String email;
    private String firstname;
    private String lastname;
    private String login;
    private LdapUser manager;
}
