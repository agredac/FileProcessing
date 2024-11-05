package tech.cagreda.adapter.out.db.mongo;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;
import tech.cagreda.adapter.out.db.mongo.repository.onebank.PaymentRequestRepository;
import tech.cagreda.application.ports.out.ReferenceTablePortOut;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReferenceTableAdapter implements ReferenceTablePortOut {


    private final PaymentRequestRepository paymentRequestRepository;

   //  private final TransactionRequestRepository transactionRequestRepository;

    @Override
    public Optional<PaymentRequestModel> findByBhnetBicAndBankFileName(String bhnetBIC, String bankFileName) {
        return paymentRequestRepository
                .findByBhnetBicAndBankFileName(bhnetBIC, bankFileName)
                ;

    }

    @Override
    public Optional<PaymentRequestModel> findByBhnetBicAndMessageId(String bhnetBIC, String messageID) {
        return paymentRequestRepository
                .findByBhnetBicAndMessageId(bhnetBIC, messageID)
                ;
    }

    @Override
    public Optional<PaymentRequestModel> findMessageId(String messageID) {
        return paymentRequestRepository.findByMessageId(messageID);
    }

/*    @Override
    public Optional<TransactionRequestModel> findByPaymentIdAndEndToEndId(String paymentID, String endToEndID) {
        return transactionRequestRepository
                .findByPaymentIdAndEndToEndId(paymentID, endToEndID)
                ;
    }

    @Override
    public Optional<TransactionRequestModel> findForAgrarioBank(String paymentID, String cdtrId, String cdtrAccount, String cdtrAmt) {
        return transactionRequestRepository.findForAgrarioBank(paymentID, cdtrId, cdtrAccount, cdtrAmt);
    }*/


}
