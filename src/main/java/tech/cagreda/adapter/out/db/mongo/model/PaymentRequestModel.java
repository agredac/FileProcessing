package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("PaymentRequest")
@Builder
public record PaymentRequestModel(
    String _id,
    String bhnetBic,
    String bankFileName,
    String corporateFileName,
    String messageId,
    Long creationDate,
    PaymentInfoModel paymentInfo) {}
