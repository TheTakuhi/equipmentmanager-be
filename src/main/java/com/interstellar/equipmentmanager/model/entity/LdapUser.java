package com.interstellar.equipmentmanager.model.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class LdapUser {
    private UUID objectGUID;
    private String email;
    private String firstname;
    private String lastname;
    private String userPrincipalName;
    private String thumbnailPhoto;
}
