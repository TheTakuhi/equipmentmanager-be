package com.interstellar.equipmentmanager.security;

import com.interstellar.equipmentmanager.model.dto.user.in.UserCreateDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.service.UserService;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserService userService;
    public boolean verifyToken(String token){
        return token != null && token.startsWith("Bearer ");
    }

    public List<UserRole> extractRoles(Map<String, Object> claims) {
        Gson gson = new Gson();
        String json = gson.toJson(claims.get("resource_access"));
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        String jsonRoles = jsonObject.get("equipment-manager-fe").getAsJsonObject().get("roles").toString();
        List<UserRole> roles = new ArrayList<>();
        gson.fromJson(jsonRoles, List.class).forEach(role -> roles.add(
                UserRole.valueOf((String) role)
        ));
        return roles;
    }

    public Map<String, Object> decode(String token){
        if (verifyToken(token)) {
            String jwkSetUri = "https://staging.int.tieto.com/keycloak/auth/realms/staging-realm/protocol/openid-connect/certs";
            String jwt_only = token.substring(7);
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            Jwt jwt = jwtDecoder.decode(jwt_only);
            Map<String, Object> claims = jwt.getClaims();
            return claims;
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            try{
                handleUserFromToken(
                        decode(authorizationHeader)
                );
            } catch (JwtException e) {
                throw new JwtException("Invalid JWT");
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleUserFromToken(Map<String, Object> claims){
        try{
            userService.getUserByLogin(claims.get("preferred_username").toString());
        } catch (NotFoundException e){
            UserCreateDTO userCreateDTO = new UserCreateDTO();
            userCreateDTO.setId(UUID.fromString(claims.get("LDAP_ID").toString()));
            userCreateDTO.setFirstName(claims.get("preferred_username").toString());
            userCreateDTO.setFirstName(claims.get("given_name").toString());
            userCreateDTO.setLastName(claims.get("family_name").toString());
            userCreateDTO.setEmail(claims.get("email").toString());
            userCreateDTO.setUserRoles(extractRoles(claims));
            userService.createUser(userCreateDTO);
        }
    }
}
