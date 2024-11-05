package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("TransactionRequest")
@Builder
public record TransactionRequestModel(
    String id,
    String paymentId,
    String endToEndId,
    TransactionRecordModel transactionRecord,
    String instructionID,
    Long transactionDate) {}
