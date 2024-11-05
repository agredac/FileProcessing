package tech.cagreda.adapter.out.db.mongo.model;

import lombok.Builder;

@Builder
public record TransactionRecordModel(
    String ctgyPurp,
    AmtModel amt,
    CdtrAgtModel cdtrAgt,
    CdtrModel cdtr,
    CdtrAcctModel cdtrAcct,
    RmtInfModel rmtInf) {}
