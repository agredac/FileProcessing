package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RmtdAmtModel(BigDecimal value, String ccy) {}
