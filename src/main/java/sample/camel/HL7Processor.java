package sample.camel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;

@Component
public class HL7Processor {
    

    public String extractPatientId(Exchange exchange) {
	    Message m = exchange.getIn().getBody(Message.class);
	    Terser t = new Terser(m);
	    String pid31="XXX";
	    try {
		pid31 = t.get("PID-3-1");
		t.set("PID-3-1","new");
		exchange.getIn().setHeader("PID31a",pid31);
		exchange.getIn().setBody(m);
		exchange.getIn().setHeader("PID31b",t.get("PID-3-1"));
	    } catch (Exception e) {
		System.err.println("Error getting pid31 from message body (hl7)");
	    }
	    try {
		exchange.setProperty("CamelMllpAcknowledgementString",
				     "MSH|^~\\&|RS|RF|SA|SF|20250328140203.319-0700||ACK^A01|MSG00001A|P|2.5.1\rMSA|AA|MSG00001");
	    } catch (Exception e) {
		System.err.println("Error setting property CamelMllpAcknowledgementString");
	    }
	    return pid31;
    }
}
