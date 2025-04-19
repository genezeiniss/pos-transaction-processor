package com.genezeiniss.pos_transaction_processor.configuration;

import org.jooq.conf.RenderNameCase;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionProcessorApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public DefaultConfigurationCustomizer jooqConfigurationCustomizer() {
        return c -> c.settings()
                .withRenderNameCase(RenderNameCase.LOWER);
    }
}
