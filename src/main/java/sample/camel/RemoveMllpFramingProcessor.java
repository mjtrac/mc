import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("RemoveMllpFramingProcessor")
public class RemoveMllpFramingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String response = exchange.getIn().getBody(String.class);
if (response != null && !response.isEmpty()) {
            char START_BLOCK = 0x0B;  // \u000B
            char END_BLOCK = 0x1C;    // \u001C
            char CARRIAGE_RETURN = 0x0D; // \u000D

            // Remove leading Start Block (0x0B)
            if (response.charAt(0) == START_BLOCK) {
                response = response.substring(1);
            }

            // Remove trailing End Block (0x1C) + Carriage Return (0x0D)
            if (response.endsWith("" + END_BLOCK + CARRIAGE_RETURN)) {
                response = response.substring(0, response.length() - 2);
            }
        }

        exchange.getIn().setBody(response);
    }
}
