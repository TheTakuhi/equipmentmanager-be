package com.interstellar.equipmentmanager.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "managers")
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    @Id
    private UUID id;

    private String login;

    private String email;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "manager")
    private List<User> managedUsers;
}
