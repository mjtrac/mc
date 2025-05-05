/*
 * Copyright 2025 Mitch Trachtenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("MllpProcessor")  
public class MllpProcessor {

    @Autowired  
    private ProducerTemplate producerTemplate;

    private static final Logger log = LoggerFactory.getLogger(MllpProcessor.class);
    

    public void callInnerMllp(Exchange exchange) {  
        String originalMessage = exchange.getIn().getBody(String.class);
	System.out.println("Entering callInnerMllp with: \n" + originalMessage + "\n***\n");
        // Invoke the inner MLLP route and wait for the response.
	System.out.println("Calling producerTemplate.request with orig msg");
        //String r  = producerTemplate.requestBody("mllp://0?hostname=localhost&port=15682&autoAck=false", originalMessage, String.class);
	Exchange responseExchange = producerTemplate.request("mllp://localhost:15682?autoAck=false&exchangePattern=InOut&requireEndOfData=false", exchange1 -> {
            exchange1.getIn().setBody(originalMessage);
        });

        // Extract the response (ACK) from port B
	log.info("responseExchange properties");
	String mllpAck = responseExchange.getProperty("CamelMllpAcknowledgement", String.class);
	String mllpAckString = responseExchange.getProperty("CamelMllpAcknowledgementString", String.class);
	String mllpAckType = responseExchange.getProperty("CamelMllpAcknowledgementType", String.class);
	log.info("CamelMllpAcknowledgement: {}", mllpAck);
	log.info("CamelMllpAcknowledgementString: {}", mllpAckString);
	log.info("CamelMllpAcknowledgementType: {}", mllpAckType);
	exchange.setProperty("CamelMllpAcknowledgementString",mllpAck);
    }

}
