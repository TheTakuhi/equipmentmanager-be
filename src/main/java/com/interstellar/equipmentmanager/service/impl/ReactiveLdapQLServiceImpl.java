package com.interstellar.equipmentmanager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interstellar.equipmentmanager.exception.InternalServerErrorException;
import com.interstellar.equipmentmanager.model.dto.request.LdapUser;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.StringTemplate.STR;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactiveLdapQLServiceImpl implements ReactiveLdapQLService {

    @Qualifier("ldapQLClient")
    private final WebClient client;

    @Override
    public @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email) {


        var query = STR."""
            query {
                findActiveDirectoryUsersByEmail(email: \{email}) {
                    objectGUID
                    lastname
                    firstname
                    email
                    login
                }
            }""";

        return client.post()
                .uri("/graphql")
                .bodyValue(createBodyValue(query))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<QueryResponse<FindUsersByEmail>>() {
                })
                .onErrorResume(WebClientException.class, (ex) -> {log.info("Error happened {}", ex.getMessage()); return Mono.empty();})
                .map(r -> {
                    if (r.data == null || r.data.findActiveDirectoryUsersByEmail == null) return new ArrayList<>();
                    return Arrays.stream(r.data.findActiveDirectoryUsersByEmail).collect(Collectors.toList());
                });
    }

    @Override
    public Flux<List<LdapUser>> findAllUsers() {

        var query = """
            query {
                findAllActiveDirectoryUsers {
                    objectGUID
                    lastname
                    firstname
                    email
                    login
                }
            }
        """;

        return this.client.post()
                .uri("/graphql")
                .bodyValue(createBodyValue(query))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<QueryResponse<FindAllUsers>>() {})
                .onErrorResume(WebClientException.class, (ex) -> {log.info("Error happened {}", ex.getMessage()); return Mono.empty();})
                .map(r -> {
                    if (r.data == null || r.data.findAllActiveDirectoryUsers == null) return new ArrayList<>();
                    return Arrays.stream(r.data.findAllActiveDirectoryUsers).collect(Collectors.toList());
                });
    }

    @Override
    public Mono<List<LdapUser>> findAllUsersNotIn(List<User> users)  {
        StringBuilder uuids = new StringBuilder();

        for (User user : users) {
            uuids.append(user.getId()).append(",");
        }

        if (!users.isEmpty()) {
            uuids.deleteCharAt(uuids.length() - 1);
        }

        String query = STR."""
                query {
                    findAllOthersActiveDirectoryUsers(objectGUIDs: "\{uuids.toString()}") {
                        objectGUID
                        lastname
                        firstname
                        email
                        login
                    }
                }
               """;

        return client.post()
                .uri("/graphql")
                .bodyValue(createBodyValue(query))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<QueryResponse<FindAllOthersActiveDirectoryUsers>>() {
                })
                .onErrorResume(WebClientException.class, (ex) -> {log.info("Error happened {}", ex.getMessage()); return Mono.empty();})
                .map(r -> {
                    if (r.data == null || r.data.findAllOthersActiveDirectoryUsers == null) return new ArrayList<>();
                    return Arrays.stream(r.data.findAllOthersActiveDirectoryUsers).collect(Collectors.toList());
                });
    }

    private String createBodyValue(String query) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(Map.of("query", query));
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("JSON mapping error occurred in graphql query");
        }
    }

    private record QueryResponse<T>(T data) {
    }

    private record FindUsersByEmail(LdapUser[] findActiveDirectoryUsersByEmail) {
    }

    private record FindAllUsers(LdapUser[] findAllActiveDirectoryUsers) {
    }

    private record FindAllOthersActiveDirectoryUsers(LdapUser[] findAllOthersActiveDirectoryUsers) {
    }
}
