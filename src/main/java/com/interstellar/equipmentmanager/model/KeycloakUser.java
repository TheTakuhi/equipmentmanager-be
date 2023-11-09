package com.interstellar.equipmentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeycloakUser {
    private Object id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String photo;
}
