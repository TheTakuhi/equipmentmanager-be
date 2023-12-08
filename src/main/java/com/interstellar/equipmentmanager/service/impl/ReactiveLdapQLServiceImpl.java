package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.request.LdapUser;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactiveLdapQLServiceImpl implements ReactiveLdapQLService {
    @Value("${ldapQL.auth-token}")
    private String authToken;

    @Value("${ldapQL.base-url}")
    private String baseUrl;

    @Override
    public @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email) {
        var client = WebClient.builder()
                .filters(exchangeFilterFunctions ->
                        exchangeFilterFunctions.addAll(List.of(logRequest(), logResponse())))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        var ql2 = "query { findActiveDirectoryUsersByEmail(email: \\\"%s\\\") ".formatted(email) +
                "{ objectGUID lastname firstname email thumbnailPhoto userPrincipalName } }";

        return client.post()
                .uri("/graphql")
                .header("auth-token", authToken)
                .bodyValue("{\"query\": \"%s\"}".formatted(ql2))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<QueryResponse<FindUsersByEmail>>() {
                })
                .onErrorResume(WebClientException.class, (ex) -> {log.info("Error happened {}", ex.getMessage()); return Mono.empty();})
                .map(r -> {
                    if (r.data == null || r.data.findActiveDirectoryUsersByEmail == null) return  new ArrayList<>();
                    return Arrays.stream(r.data.findActiveDirectoryUsersByEmail).collect(Collectors.toList());
                });
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Web client sent url: {}", clientRequest.url().getPath());
            return Mono.just(clientRequest);
        });
    }

    private  ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Web client sent url: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }


    private record QueryResponse<T>(T data) {
    }

    private record FindUsersByEmail(LdapUser[] findActiveDirectoryUsersByEmail) {
    }
}
