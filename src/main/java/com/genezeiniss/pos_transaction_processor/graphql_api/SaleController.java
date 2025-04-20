package com.genezeiniss.pos_transaction_processor.graphql_api;

import com.genezeiniss.pos_transaction_processor.graphql_api.model.mapper.TransactionProcessorModelMapper;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SaleRequest;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.response.SaleResponse;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.response.SalesReportResponse;
import com.genezeiniss.pos_transaction_processor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final TransactionProcessorModelMapper transactionModelMapper;
    private final TransactionService transactionService;

    // todo: implement input validation at the API layer
    //  Add rate limiting for transaction processing
    @MutationMapping
    public SaleResponse processSale(@Argument SaleRequest request) {

        var transaction = transactionModelMapper.mapSaleRequestToTransaction(request);
        var transactionMetadata = transactionModelMapper.mapToTransactionMetadata(request.getAdditionalItem());
        transactionService.processTransaction(transaction, transactionMetadata);
        return transactionModelMapper.mapTransactionToSaleResponse(transaction);
    }

    @QueryMapping
    public SalesReportResponse getSalesReport(@Argument String startDateTime, @Argument String endDateTime) {

        return new SalesReportResponse();
    }
}
