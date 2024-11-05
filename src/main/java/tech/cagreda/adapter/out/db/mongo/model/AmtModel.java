package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record AmtModel(String value, String ccy) {}
