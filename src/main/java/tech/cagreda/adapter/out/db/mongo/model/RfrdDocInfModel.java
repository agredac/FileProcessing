package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record RfrdDocInfModel(String tp, Long rltdDt) {}
