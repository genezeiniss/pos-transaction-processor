package com.genezeiniss.pos_transaction_processor.repository;

import com.genezeiniss.point_of_sale.persistence.generated.tables.records.TransactionRecord;
import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import org.jooq.Table;
import org.springframework.stereotype.Repository;

import static com.genezeiniss.point_of_sale.persistence.generated.Tables.TRANSACTION;

@Repository
public class TransactionRepository extends BaseRepository<Transaction, TransactionRecord> {

    @Override
    public Table<TransactionRecord> getDBTable() {
        return TRANSACTION;
    }

    @Override
    public Class<Transaction> getEntityType() {
        return Transaction.class;
    }
}
