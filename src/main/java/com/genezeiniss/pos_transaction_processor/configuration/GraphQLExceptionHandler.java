package com.genezeiniss.pos_transaction_processor.configuration;

import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.modelmapper.MappingException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Optional;

@ControllerAdvice
public class GraphQLExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleException(ValidationException exception, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .message(exception.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(java.util.Map.of(
                        "error", exception.getMessage()))
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleException(MappingException exception, DataFetchingEnvironment env) {
        var errorMessage = Optional.ofNullable(exception.getCause().getMessage())
                .orElse(exception.getMessage());
        return GraphqlErrorBuilder.newError()
                .message(exception.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(java.util.Map.of(
                        "error", errorMessage))
                .build();
    }
}
