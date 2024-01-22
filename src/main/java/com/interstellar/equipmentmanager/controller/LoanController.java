package com.interstellar.equipmentmanager.controller;

import com.interstellar.equipmentmanager.model.dto.CustomPageDTO;

import com.interstellar.equipmentmanager.model.dto.loan.in.LoanCreateDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanDTO;
import com.interstellar.equipmentmanager.model.enums.Type;
import com.interstellar.equipmentmanager.model.filter.LoanFilter;
import com.interstellar.equipmentmanager.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Endpoints for managing loans")
@SecurityRequirement(name = "keycloak")
@Validated
@Slf4j
public class LoanController {


    private final LoanService loanService;

    @Operation(summary = "Create new loan", responses = {
            @ApiResponse(
                    description = "Loan has been created successfully",
                    responseCode = "201",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Loan with this item is already active",
                    responseCode = "409",
                    content = @Content
            )
    }, description = "Create loan with given Item and User")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public LoanDTO createLoan(@RequestBody LoanCreateDTO loanCreateDTO) {
        return loanService.createLoan(loanCreateDTO);
    }


    @Operation(summary = "Get Loan by id", responses = {
            @ApiResponse(
                    description = "Get loan is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Loan has not been found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Get Loan by its id")
    @GetMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public LoanDTO getLoan(@PathVariable UUID id) {

        return loanService.getLoanDTOById(id);
    }


    @Operation(summary = "Return loan by id", responses = {
            @ApiResponse(
                    description = "Return loan is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Loan has not been found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Return Loan by its id and return date")
    @PatchMapping("/{id}/{date}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public LoanDTO returnLoan(@PathVariable UUID id, @PathVariable LocalDate date) {

        return loanService.returnLoan(id, date);
    }


    @Operation(summary = "Get loans by filter", responses = {
            @ApiResponse(
                    description = "Get loans successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            )
    }, description = "Only ADMIN can see all loans")
    @GetMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public CustomPageDTO<LoanDTO> getLoans(
            @Parameter(description = "Search loan by item serial code", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String serialCode,
            @Parameter(description = "Search by item type", schema = @Schema(implementation = Type.class))
            @RequestParam(required = false) Type type,
            @Parameter(description = "Search loan by borrower name or login", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String borrowerName,
            @Parameter(description = "Search loan by lender name or login", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String lenderName,
            @PageableDefault(size = 50) Pageable pageable) {
        var filter = new LoanFilter(borrowerName, lenderName, type, serialCode);
        var standardPage = loanService.getAllLoans(filter, pageable);
        return new CustomPageDTO<>(
                standardPage.getTotalElements(),
                standardPage.getTotalPages(),
                standardPage.getContent().size(),
                standardPage.getContent(),
                pageable,
                standardPage.getTotalPages() > pageable.getPageNumber() + 1,
                pageable.getPageNumber() > 0
        );
    }

}
