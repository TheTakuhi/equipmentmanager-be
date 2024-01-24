package com.interstellar.equipmentmanager.model.entity;

import com.interstellar.equipmentmanager.model.enums.ContractType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "contracts")
@EntityListeners(AuditingEntityListener.class)
public class Contract {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "contract_id", length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    private User contractOwner;

    @ManyToOne(optional = true)
    private User contractManager;
}
