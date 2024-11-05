package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentInfoModel(String debtorName, String creditDateTime,
                               Long numberOfTxs, BigDecimal ctrlSum,
                               String debtorID, String accontNumber, String accountType, String executionDate, String pmtInfId) {}
