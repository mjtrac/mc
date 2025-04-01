import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;


@Component("AddMllpFramingProcessor")
public class AddMllpFramingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String hl7Message = exchange.getIn().getBody(String.class);
       // MLLP framing characters
        char START_BLOCK = 0x0B;  // Hex for \u000B
        char END_BLOCK = 0x1C;    // Hex for \u001C
        char CARRIAGE_RETURN = 0x0D; // Hex for \u000D

        // Wrap message with MLLP framing
        String framedMessage = START_BLOCK + hl7Message + END_BLOCK + CARRIAGE_RETURN;
        exchange.getIn().setBody(framedMessage);
    }
}
