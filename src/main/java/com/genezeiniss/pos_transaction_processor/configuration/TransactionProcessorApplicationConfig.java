package com.genezeiniss.pos_transaction_processor.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionProcessorApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
