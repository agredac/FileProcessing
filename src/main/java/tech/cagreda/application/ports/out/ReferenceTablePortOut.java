package tech.cagreda.application.ports.out;



import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;

import java.util.Optional;

public interface ReferenceTablePortOut {

  Optional<PaymentRequestModel> findByBhnetBicAndBankFileName(String bhnetBIC, String bankFileName) ;


  Optional<PaymentRequestModel> findByBhnetBicAndMessageId(String bhnetBIC, String messageID);

  Optional<PaymentRequestModel> findMessageId(String messageID);

/*  Optional<TransactionRequestModel> findByPaymentIdAndEndToEndId(String paymentID, String endToEndID);

  Optional<TransactionRequestModel> findForAgrarioBank(String paymentID, String cdtrId, String cdtrAccount, String cdtrAmt);*/


}
