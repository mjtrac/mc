package sample.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class MllpAckProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        byte[] ack = in.getBody(byte[].class); // Get the acknowledgement
	exchange.setProperty("CamelMllpAcknowledgement",ack);
        exchange.getOut().setBody(ack); //Set the acknowledgement to return to the original sender.
	System.out.println("processAck: " + ack.length);
    }
}
