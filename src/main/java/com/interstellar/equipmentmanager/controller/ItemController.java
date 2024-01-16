package com.interstellar.equipmentmanager.controller;


import com.interstellar.equipmentmanager.model.dto.CustomPageDTO;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemCreateDTO;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemEditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import com.interstellar.equipmentmanager.model.filter.ItemFilter;
import com.interstellar.equipmentmanager.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Endpoints for managing items")
@SecurityRequirement(name = "keycloak")
@Validated
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Create new item", responses = {
            @ApiResponse(
                    description = "Item has been created successfully",
                    responseCode = "201",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Item with this serial number already exists",
                    responseCode = "409",
                    content = @Content
            )
    }, description = "Create item with currently logged user as owner")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER') || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN')")
    public ItemDTO createItem(@RequestBody ItemCreateDTO itemCreateDTO) {
        return itemService.createItem(itemCreateDTO);
    }

    @Operation(summary = "Update item", responses = {
            @ApiResponse(
                    description = "Update of item is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "item is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Update item by id")
    @PutMapping("/{id}")
    @PreAuthorize("@itemServiceImpl.isOwner(#id) || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN') ")
    public ItemDTO updateItem(
            @PathVariable UUID id,
            @Valid @RequestBody ItemEditDTO itemEditDTO) {
        return itemService.updateItem(id, itemEditDTO);
    }


    @Operation(summary = "Delete item from local database", responses = {
            @ApiResponse(
                    description = "Delete of item is successful",
                    responseCode = "204",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Item has not been found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only ADMIN and owner can delete Item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.isOwner(#id)" +
            " || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN')")
    public void deleteItem(@PathVariable UUID id) {
        itemService.deleteItemById(id);
    }

    @Operation(summary = "Get items by filter", responses = {
            @ApiResponse(
                    description = "Get items successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            )
    }, description = "Only MANAGER and ADMIN can see all items")
    @GetMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public CustomPageDTO<ItemDTO> getItems(
            @Parameter(description = "Search by item serial code", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String serialCode,
            @Parameter(description = "Search by item type", schema = @Schema(implementation = Type.class))
            @RequestParam(required = false) Type type,
            @Parameter(description = "Search by item state", schema = @Schema(implementation = State.class))
            @RequestParam(required = false) State state,
            @Parameter(description = "Search by item quality state", schema = @Schema(implementation = QualityState.class))
            @RequestParam(required = false) QualityState qualityState,
            @PageableDefault(size = 50) Pageable pageable) {
        var filter = new ItemFilter(serialCode, type, state, qualityState);
        var standardPage = itemService.getAllItems(filter, pageable);
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


    @Operation(summary = "Get Item by id", responses = {
            @ApiResponse(
                    description = "Return item successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Item has not been found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Get Item by its id")
    @GetMapping("/{id}}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public ItemDTO getItem(@PathVariable UUID id) {

        return itemService.getItemById(id);
    }
}