package sample.camel;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component("terserUtil")
public class TerserUtil  {

 /**
     * Extracts a value from the HL7 message body using a Terser path.
     * @param message the HL7 message body
     * @param terserPath the Terser path to extract
     * @return the extracted value, or null if missing
     * @throws Exception if parsing fails
     */
    @Handler
    public String extract(Message message, String terserPath) throws Exception {
        if (message != null && terserPath != null) {
            Terser terser = new Terser(message);
	    String result = terser.get(terserPath);
	    return result;
        }
        return null;
    }
}    
