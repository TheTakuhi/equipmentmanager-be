package com.interstellar.equipmentmanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setCollectionsMergeEnabled(false)
                .setAmbiguityIgnored(true);

        return mapper;
    }
}
