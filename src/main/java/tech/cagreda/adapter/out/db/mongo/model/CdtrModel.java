package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record CdtrModel(String nm, String id, PostalAddressModel postalAddress, String schmeNm) {}
