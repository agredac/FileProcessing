package tech.cagreda.adapter.out.db.mongo.repository.onebank;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;

import java.util.Optional;

@Repository
public interface PaymentRequestRepository extends MongoRepository<PaymentRequestModel, String> {


    Optional<PaymentRequestModel> findByBhnetBicAndBankFileName(String bhnetBIC, String bankFileName);

    Optional<PaymentRequestModel> findByBhnetBicAndMessageId(String bhnetBIC, String messageID);

    Optional<PaymentRequestModel> findByMessageId(String messageID);


}
