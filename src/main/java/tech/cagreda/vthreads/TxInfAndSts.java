package tech.cagreda.vthreads;

import com.latam.ecs.canonical.model.paymentresponses.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;


 @XmlRootElement(name = "TxInfAndSts")
@XmlAccessorType(XmlAccessType.FIELD)
/*@XmlType(name = "TxInfAndSts")*/
public class TxInfAndSts {


  @XmlElement(name = "OrgnlEndToEndId")
  private String orgnlEndToEndId;

  public TxInfAndSts() {}


  public String getOrgnlEndToEndId() {
    return this.orgnlEndToEndId;
  }

  public void setOrgnlEndToEndId(String value) {
    this.orgnlEndToEndId = value;
  }

}
