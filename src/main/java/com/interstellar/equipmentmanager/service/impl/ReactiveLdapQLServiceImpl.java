package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.dto.user.out.UserHierarchyDTO;
import com.interstellar.equipmentmanager.model.entity.Manager;
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
    
    private final static String BASE_ATTRIBUTES = """
            fragment BaseAttr on ActiveDirectoryUser {
                 id: objectGUID
                 firstName
                 lastName
                 login
                 email
               }
            """;
    
    @Override
    public @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email) {
        var query = """
                    %s
                    query findActiveDirectoryUsersByEmail($email: String!) {
                        findActiveDirectoryUsersByEmail(email: $email) {
                           ...BaseAttr
                        }
                    }
                """.formatted(BASE_ATTRIBUTES);
        
        return client.document(query)
                .variable("email", email)
                .retrieve("findActiveDirectoryUsersByEmail")
                .toEntityList(LdapUser.class);
    }

    @Override
    public Mono<List<LdapUser>> findAllUsers() {
        var query = """
                    %s
                    query {
                        findAllActiveDirectoryUsers {
                            ...BaseAttr
                            manager {
                                id: objectGUID
                            }
                        }
                    }
                """.formatted(BASE_ATTRIBUTES);
        
        return client.document(query)
                .retrieve("findAllActiveDirectoryUsers")
                .toEntityList(LdapUser.class);
    }
    
    @Override
    public Mono<List<Manager>> findAllManagers() {
        var query = """
                    %s
                    query {
                        findAllManagers {
                            ...BaseAttr
                        }
                    }
                """.formatted(BASE_ATTRIBUTES);
        
        return client.document(query)
                .retrieve("findAllManagers")
                .toEntityList(Manager.class);
    }
    
    @Override
    public Mono<List<LdapUser>> findAllUsersNotIn(List<User> users) {
        String query = """
                 %s
                 query findUsers($ids: [ID]!) {
                   findAllOthersActiveDirectoryUsers(objectGUIDs: $ids) {
                     ...BaseAttr
                     manager {
                        ...BaseAttr
                      }
                   }
                 }
                """.formatted(BASE_ATTRIBUTES);
        
        return client.document(query)
                .variable("ids", users.stream().map(User::getId).toList())
                .retrieve("findAllOthersActiveDirectoryUsers")
                .toEntityList(LdapUser.class);
    }
    
    @Override
    public Mono<UserHierarchyDTO> getHierarchyOfUser(UUID id) {
        String query = """
                %s
               query findUser($id: ID!) {
                  findOneActiveDirectoryUserByObjectGUID(objectGUID: $id) {
                     ...BaseAttr
                     photo: thumbnailPhoto
                     fullName
                     manager {
                        ...BaseAttr
                        photo: thumbnailPhoto
                        fullName
                        manager {
                           ...BaseAttr
                           photo: thumbnailPhoto
                           fullName
                           manager {
                              ...BaseAttr
                              photo: thumbnailPhoto
                              fullName
                              manager {
                                 ...BaseAttr
                                 photo: thumbnailPhoto
                                 fullName
                                 manager {
                                    ...BaseAttr
                                    photo: thumbnailPhoto
                                    fullName
                                 }
                              }
                           }
                        }
                     }
                  }
               }
               """.formatted(BASE_ATTRIBUTES);
        
        return client.document(query)
                .variable("id", id)
                .retrieve("findOneActiveDirectoryUserByObjectGUID")
                .toEntity(UserHierarchyDTO.class);
    }
}