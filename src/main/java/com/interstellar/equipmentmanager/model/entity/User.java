package com.interstellar.equipmentmanager.model.entity;

import com.interstellar.equipmentmanager.model.enums.AuditActionType;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(length = 36, name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "login")
    @NotNull
    @NotBlank
    private String login;
    
    @Column(name = "email")
    @NotNull
    private String email;
    
    @Column(name = "first_name")
    @NotNull
    @NotBlank
    private String firstName;
    
    @Column(name = "last_name")
    @NotNull
    @NotBlank
    private String lastName;
    
    @Column(name = "full_name")
    @NotNull
    @NotBlank
    private String fullName;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String photo;
    
    @ManyToOne
    private Manager manager;
    
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role")
    @Enumerated(EnumType.STRING)
    @Column(name = "\"role\"", nullable = false)
    @NotNull
    private List<UserRole> userRoles;
    
    @ManyToMany
    @JoinTable(
            name = "user_teams",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "teams_id"))
    private List<Team> teams;
    
    @OneToMany(mappedBy = "owner")
    private List<Team> ownedTeams;
    
    @OneToMany(mappedBy = "owner")
    private List<Item> ownedItems;
    
    @OneToMany(mappedBy = "borrower")
    private List<Loan> borrowings;
    
    @OneToMany(mappedBy = "lender")
    private List<Loan> loans;
    
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
        this.auditInfo = new AuditInfo(
                Instant.now(),
                Instant.now(),
                AuditActionType.SYNC_SYSTEM,
                AuditActionType.SYNC_SYSTEM
        );
    }
    
    @PreUpdate
    void preUpdate() {
        this.auditInfo = new AuditInfo(
                this.getAuditInfo().getCreatedAt(),
                Instant.now(),
                this.getAuditInfo().getCreatedBy(),
                AuditActionType.SYNC_SYSTEM
        );
    }
}