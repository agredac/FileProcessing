package tech.cagreda.vthreads;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;
import tech.cagreda.application.ports.out.ReferenceTablePortOut;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EnrichmentProcessor implements ItemProcessor<String, String> {

    private final MongoTemplate mongoTemplate;

    private final ReferenceTablePortOut referenceTablePortOut;


    @Override
    public String process(String line) throws Exception {
        String[] fields = line.split(",");
        String id = fields[0];  // Assuming ID is in the first colum'n

        Optional<PaymentRequestModel> messageId = referenceTablePortOut.findMessageId("0001");

        // Fetch data from MongoDB based on ID
        Document result = mongoTemplate.getCollection("yourCollection").find(new Document("id", id)).first();

        if (messageId.isPresent()) {
            return line + "," + result.getString("mongoField1") + "," + result.getString("mongoField2");
        } else {
            return line + ",,"; // If no MongoDB data found
        }
    }
}
