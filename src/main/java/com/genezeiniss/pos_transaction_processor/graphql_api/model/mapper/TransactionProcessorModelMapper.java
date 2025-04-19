package com.genezeiniss.pos_transaction_processor.graphql_api.model.mapper;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SaleRequest;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.response.SaleResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionProcessorModelMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper.createTypeMap(SaleRequest.class, Transaction.class)
                .addMapping(SaleRequest::getPrice, Transaction::setOriginalPrice)
                .addMapping(SaleRequest::getDatetime, Transaction::setCreatedAt);
    }

    public Transaction mapTransactionRequestToTransaction(SaleRequest saleRequest) {
        return modelMapper.map(saleRequest, Transaction.class);
    }

    public List<TransactionMetadata> mapToTransactionMetadata(Map<String, String> additionalItems) {
        return Optional.ofNullable(additionalItems).map(
                        items -> items.entrySet().stream()
                                .map(entry -> toTransactionMetadata(entry.getKey(), entry.getValue()))
                                .toList())
                .orElse(Collections.emptyList());
    }

    private TransactionMetadata toTransactionMetadata(String attribute, String data) {
        var transactionMetadata = new TransactionMetadata();
        transactionMetadata.setAttribute(attribute);
        transactionMetadata.setData(data);
        return transactionMetadata;
    }

    public SaleResponse mapTransactionToTransactionResponse(Transaction transaction) {
        return modelMapper.map(transaction, SaleResponse.class);
    }
}
