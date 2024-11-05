package tech.cagreda.xmlbatch;

import com.latam.ecs.canonical.model.paymentresponses.Document;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class XmlBatchConfig {

    @Bean
    public StaxEventItemReader<Document> reader() {
        StaxEventItemReader<Document> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource("CANONICALPain002V12.xml"));
        reader.setFragmentRootElementName("OrgnlGrpInfAndSts"); // Root element for each record


        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Document.class); // Bind XML to Employee class
        reader.setUnmarshaller(marshaller);

        return reader;
    }
}

