package com.genezeiniss.pos_transaction_processor.repository;

import com.genezeiniss.point_of_sale.persistence.generated.tables.records.TransactionMetadataRecord;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import org.jooq.Table;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.genezeiniss.point_of_sale.persistence.generated.Tables.TRANSACTION_METADATA;

@Repository
public class TransactionMetadataRepository extends BaseRepository<TransactionMetadata, TransactionMetadataRecord> {

    @Override
    public Table<TransactionMetadataRecord> getDBTable() {
        return TRANSACTION_METADATA;
    }

    @Override
    public Class<TransactionMetadata> getEntityType() {
        return TransactionMetadata.class;
    }

    public List<TransactionMetadata> findByTransactionId(String transactionId) {
        return find(selectFrom()
                .where(TRANSACTION_METADATA.TRANSACTION_ID.eq(UUID.fromString(transactionId))))
                .toList();
    }
}
