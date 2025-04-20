package com.genezeiniss.pos_transaction_processor.graphql_api;

import com.genezeiniss.pos_transaction_processor.graphql_api.model.mapper.TransactionProcessorModelMapper;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SaleRequest;
import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SalesReportRequest;
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
    public SalesReportResponse getSalesReport(@Argument SalesReportRequest request) {

        // TODO: Implement sales reporting using the following optimized approach:
        //  1. MATERIALIZED VIEW FOR HOURLY AGGREGATIONS
        //    - Create a materialized view named 'hourly_sales_summary' that stores aggregated data
        //      for the latest 30-day rolling window
        //     - Use truncated to hour precision
        //    - Include appropriate indexes on the hour column for fast querying
        //  2. SCHEDULED VIEW MAINTENANCE
        //    - Implement a Spring Scheduler (@Scheduled) with cron expression "0 0 * * * *"
        //      (runs at the top of every hour)
        //    - Refresh strategy:
        //      a) First refresh the materialized view completely (REFRESH MATERIALIZED VIEW)
        //      b) Then implement window slicing:
        //         - Add new records for the latest hour
        //         - Remove records older than 30 days
        //    - Use CONCURRENTLY option to avoid locking:
        //      REFRESH MATERIALIZED VIEW CONCURRENTLY hourly_sales_summary
        //  3. HYBRID QUERY LOGIC
        //    - When querying sales data:
        //      a) First check the materialized view for historical data (0-30 days old)
        //      b) For real-time data (<1 hour old) not yet in the view:
        //         - Fall back to querying the raw transaction table
        //         - Combine results from both sources in the application layer
        return new SalesReportResponse();
    }
}
