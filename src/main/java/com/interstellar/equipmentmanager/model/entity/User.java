package com.interstellar.equipmentmanager.model.entity;

import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(length = 36, name = "user_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "ldap_number")
    private UUID ldapId;

    @Column(name = "login")
    @NotNull
    @NotBlank
    private String login;

    @Column(name = "email")
    @NotNull
    @NotBlank
    @Email
    private String email;

    @Column(name = "first_name")
    @NotNull
    @NotBlank
    @AlphaString
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @NotBlank
    @AlphaString
    private String lastName;

    @Column(name = "full_name")
    @NotNull
    @NotBlank
    @AlphaString
    private String fullName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String photo;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "contractOwner", orphanRemoval = true)
    private List<Contract> ownedContracts;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    @Column(name = "removed")
    private boolean removed = false;

    @PrePersist
    void prePersist() {
        this.setFullName(
                String.format("%s %s",
                        firstName == null ? "x" : firstName,
                        lastName == null ? "x" : lastName)
        );
    }
}
