package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactiveLdapQLServiceImpl implements ReactiveLdapQLService {

    @Qualifier("ldapQLClient")
    private final GraphQlClient client;

    @Override
    public @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email) {
        var query = """
                query findActiveDirectoryUsersByEmail($email: String!) {
                    findActiveDirectoryUsersByEmail(email: $email) {
                        objectGUID
                        lastname
                        firstname
                        email
                        login
                    }
                }""";

        return client.document(query)
                .variable("email", email)
                .retrieve("findActiveDirectoryUsersByEmail")
                .toEntityList(LdapUser.class);
    }

    @Override
    public Mono<List<LdapUser>> findAllUsers() {
        var query = """
                    query {
                        findAllActiveDirectoryUsers {
                            objectGUID
                            lastname
                            firstname
                            email
                            login
                            manager {
                                objectGUID
                                lastname
                                firstname
                                email
                                login
                            }
                        }
                    }
                """;

        return client.document(query)
                .retrieve("findAllActiveDirectoryUsers")
                .toEntityList(LdapUser.class);
    }

    @Override
    public Mono<List<LdapUser>> findAllUsersNotIn(List<User> users) {
        String query = """
                 query findUsers($ids: [ID]!) {
                   findAllOthersActiveDirectoryUsers(objectGUIDs: $ids) {
                     objectGUID
                     firstname
                     lastname
                     login
                     email
                     manager {
                        objectGUID
                        lastname
                        firstname
                        email
                        login
                      }
                   }
                 }
                """;

        return client.document(query)
                .variable("ids", users.stream().map(User::getId).toList())
                .retrieve("findAllOthersActiveDirectoryUsers")
                .toEntityList(LdapUser.class);
    }
}