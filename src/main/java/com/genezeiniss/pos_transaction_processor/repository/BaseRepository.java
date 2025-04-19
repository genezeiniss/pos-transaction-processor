package com.genezeiniss.pos_transaction_processor.repository;

import com.genezeiniss.pos_transaction_processor.domain.BaseEntity;
import com.genezeiniss.pos_transaction_processor.exception.NonUniqueResultException;
import org.jooq.Record;
import org.jooq.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseRepository<E extends BaseEntity, R extends TableRecord<?>> {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DSLContext db;

    protected abstract Table<R> getDBTable();

    protected abstract Class<E> getEntityType();

    public E create(E entity) {
        entity.setId(String.valueOf(UUID.randomUUID()));
        entity.setRecordCreatedAt(LocalDateTime.now());
        R record = db.newRecord(getDBTable());
        modelMapper.map(entity, record);
        record.insert();
        return entity;
    }

    protected Optional<E> findOne(ResultQuery<? extends Record> query) {
        List<E> convertedResults = find(query).collect(Collectors.toList());
        if (convertedResults.size() > 1) {
            throw new NonUniqueResultException(query);
        }
        return convertedResults.isEmpty() ? Optional.empty() : Optional.of(convertedResults.get(0));
    }

    protected Stream<E> find(ResultQuery<? extends Record> query) {
        return query.fetch().stream()
                .map(record -> modelMapper.map(record, getEntityType()));
    }

    protected SelectWhereStep<?> selectFrom() {
        return db.selectFrom(getDBTable());
    }
}
