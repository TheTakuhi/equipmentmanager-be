package com.interstellar.equipmentmanager.model.dto.request;

import lombok.Data;

import java.util.UUID;

/*
    Created for future use.
    Model returned by another application, that is used for syncing users from AD.
    Because using keycloak for syncing users from AD is time-consuming.
 */
@Data
public class LdapUser {
    private UUID objectGUID;
    private String email;
    private String firstname;
    private String lastname;
    private String userPrincipalName;
    private String thumbnailPhoto;
}
