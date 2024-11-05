package tech.cagreda.xmlbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;
import tech.cagreda.application.ports.out.ReferenceTablePortOut;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentProcessor implements ItemProcessor<String, PaymentRequestModel> {


    private final ReferenceTablePortOut referenceTablePortOut;

    @Override
    public PaymentRequestModel process(String id) throws Exception {

        // Fetch additional data from MongoDB and enrich the Employee object
        Optional<PaymentRequestModel> messageId = referenceTablePortOut.findMessageId(id);

        if (messageId.isPresent()) {
            System.out.println(messageId.get().bankFileName());
            return  messageId.get();
        }

        return null;
    }
}
