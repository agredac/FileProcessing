package tech.cagreda.adapter.out.db.mongo.repository.onebank;

import com.latam.ecs.bankpaymentresponsetransformer.adapter.out.db.mongo.model.TransactionRequestModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRequestRepository
    extends MongoRepository<TransactionRequestModel, String> {

    Optional<TransactionRequestModel> findByPaymentIdAndEndToEndId(String paymentID, String endToEndID);

    @Query("{'paymentId': ?0,'transactionRecord.cdtr._id': ?1,'transactionRecord.cdtrAcct.accountNumber': ?2,'transactionRecord.amt.value': ?3}")
    Optional<TransactionRequestModel> findForAgrarioBank(String paymentID, String cdtrId, String cdtrAccount, String cdtrAmt);





}
