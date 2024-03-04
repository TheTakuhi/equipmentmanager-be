package com.interstellar.equipmentmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class HttpLdapQLClientConfig {

    @Value("${ldapQL.base-url}")
    private String baseUrl;

    @Value("${ldapQL.api-key}")
    private String apiKey;

    @Bean
    public HttpGraphQlClient ldapQLClient() {
        final int size = 20 * 1024 * 1024;

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .filters(exchangeFilterFunctions ->
                        exchangeFilterFunctions.addAll(List.of(logRequest(), logResponse())))
                .baseUrl("%s/graphql".formatted(baseUrl))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", apiKey)
                .build();

        return HttpGraphQlClient.create(webClient);
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(Mono::just);
    }

    private  ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(Mono::just);
    }
}