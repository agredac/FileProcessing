package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

import java.util.List;

@Builder
public record StrdModel(
    List<RfrdDocInfModel> rfrdDocInf, DuePyblAmtModel duePyblAmt, RmtdAmtModel rmtdAmt) {}
