package com.interstellar.equipmentmanager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "teams")
@EntityListeners(AuditingEntityListener.class)
public class Team {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "team_id", length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true)
    private String teamName;

    @ManyToOne(optional = false)
    private User owner;

    @ManyToMany(mappedBy = "teams")
    private List<User> members;

}