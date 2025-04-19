package com.genezeiniss.pos_transaction_processor.configuration;

import com.genezeiniss.point_of_sale.persistence.generated.tables.records.TransactionRecord;
import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import graphql.scalars.ExtendedScalars;
import org.jooq.conf.RenderNameCase;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Configuration
public class TransactionProcessorApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true);

        // todo: consider to move date/time conversion to the base repository
        // Transaction → TransactionRecord (OffsetDateTime → LocalDateTime)
        modelMapper.createTypeMap(Transaction.class, TransactionRecord.class)
                .addMappings(mapper ->
                        mapper.using(ctx -> ((OffsetDateTime) ctx.getSource()).toLocalDateTime())
                                .map(Transaction::getCreatedAt, TransactionRecord::setCreatedAt));

        // TransactionRecord → Transaction (LocalDateTime → OffsetDateTime)
        modelMapper.createTypeMap(TransactionRecord.class, Transaction.class)
                .addMappings(mapper ->
                        mapper.using(ctx -> {
                            LocalDateTime localDateTime = (LocalDateTime) ctx.getSource();
                            return localDateTime
                                    .atOffset(ZoneOffset.systemDefault().getRules()
                                            .getOffset(localDateTime));
                        }).map(TransactionRecord::getCreatedAt, Transaction::setCreatedAt));

        return modelMapper;
    }

    @Bean
    public DefaultConfigurationCustomizer jooqConfigurationCustomizer() {
        return c -> c.settings()
                .withRenderNameCase(RenderNameCase.LOWER);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.DateTime);
    }
}
