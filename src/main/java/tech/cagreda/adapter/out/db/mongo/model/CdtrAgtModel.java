package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record CdtrAgtModel(String mmbid, String bic) {}
