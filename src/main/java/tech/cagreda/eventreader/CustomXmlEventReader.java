package tech.cagreda.eventreader;

import jakarta.xml.bind.JAXBException;
import org.springframework.batch.item.*;
import tech.cagreda.vthreads.TxInfAndSts;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomXmlEventReader implements ItemReader<TxInfAndSts> , AutoCloseable {
  private final XMLEventReader eventReader;
  private final InputStream inputStream;
 // private final String path;

  public CustomXmlEventReader(String path) throws Exception {

    this.inputStream = new FileInputStream(path);
    var xmlInputFactory = XMLInputFactory.newInstance();
    this.eventReader = xmlInputFactory.createXMLEventReader(inputStream);
  }

  @Override
  public TxInfAndSts read() throws Exception {
    TxInfAndSts item =null;

     while (eventReader.hasNext()) {
      var xmlEvent = eventReader.peek();
      if (xmlEvent.isStartElement()) {
        switch (xmlEvent.asStartElement().getName().getLocalPart()) {
          case "OrgnlEndToEndId" -> {
            item = new TxInfAndSts();
            item.setOrgnlEndToEndId(getValueFromSimpleTag(eventReader));

          }
          default -> eventReader.nextEvent();
        }
      } else      if (xmlEvent.isEndElement() && item != null && "OrgnlEndToEndId".equals(xmlEvent.asEndElement().getName().getLocalPart())) {
        return item;
      }else  eventReader.nextEvent();
    }
    eventReader.close();
    inputStream.close();
    return null; // Return the constructed item
/*    } catch (XMLStreamException  | IOException e) {
      System.out.println("Failed manipulating xml file "+e.getMessage());
      throw e;
    }*/
  }

  private String getValueFromSimpleTag(XMLEventReader reader) {
    String value = "";
    while (reader.hasNext()) {
      XMLEvent xmlEvent;
      try {
        xmlEvent = reader.nextEvent();
      } catch (XMLStreamException e) {
        return value;
      }
      if (xmlEvent.getEventType() == XMLStreamConstants.CHARACTERS) {
        return xmlEvent.asCharacters().getData();
      }
    }
    return value;
  }
  /*
  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    isOpen = true;
  }
*/

  @Override
  public void close() throws Exception {
    if (eventReader != null) {
      eventReader.close();
    }
    if (inputStream != null) {
      inputStream.close();
    }
  }
}
