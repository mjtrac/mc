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
	    return pid31;
    }
}
