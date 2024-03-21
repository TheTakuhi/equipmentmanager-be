package com.interstellar.equipmentmanager.model.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomPageDTO<T> {
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private List<T> content;
    private Pageable requestedPageable;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
}
