package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record CdtrAcctModel(String id, String tp, String accountNumber) {}
