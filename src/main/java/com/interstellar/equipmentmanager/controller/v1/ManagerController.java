package com.interstellar.equipmentmanager.controller.v1;

import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
@Tag(name = "Managers", description = "Endpoints for managing managers")
@SecurityRequirement(name = "keycloak")
@PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
public class ManagerController {
    private final ManagerService managerService;


    @Operation(summary = "EP for obtain managed people from manager",
            responses = @ApiResponse(
                    responseCode = "200",
                    useReturnTypeSchema = true
            ), description = "Only MANAGER and ADMIN do have permissions"
    )
    @GetMapping("/{id}/users")
    public List<UserDTO> getAllUsersFromManager(@PathVariable UUID id) {
        return managerService.getManagedPeople(id);
    }
}
