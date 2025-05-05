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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;

@Component
public class HL7Processor {
    
    public String generateSuccessAck(Exchange exchange) {
	    String mstr = exchange.getIn().getBody(String.class);
	    String[] segments = mstr.split("\r");
	    String mshSegment = segments[0];
	    // double backslash because pipe is a special char in regex
	    String[] mshFields = mshSegment.split("\\|");
	    StringBuilder ack = new StringBuilder();
	    try {
		ack.append(mshFields[0])
		    .append("|")
		    .append(mshFields[1])
		    .append("|")
		    .append(mshFields[4])//reverse 2,3 and 4,5 send/rcv 
		    .append("|")
		    .append(mshFields[5]) 
		    .append("|")
		    .append(mshFields[2])
		    .append("|")
		    .append(mshFields[3]) 
		    .append("|")
		    .append(mshFields[6])
		    .append("|")
		    .append(mshFields[7])
		    .append("|")
		    .append(mshFields[8])
		    .append("|")
		    .append(mshFields[9])
		    .append("|")
		    .append(mshFields[10])
		    .append("|")
		    .append(mshFields[11])
		    .append("\rMSA|AA|"+mshFields[9]+"|\r");
		exchange.setProperty(
				     "CamelMllpAcknowledgementString",
				     ack.toString());
	    } catch (Exception e) {
		System.out.println("Error setting CamelMllpAcknowledgementString property");
	    }
	    return ack.toString();
    }
		
    public void extractPatientId(Exchange exchange) {
	    Message m = exchange.getIn().getBody(Message.class);
	    Terser t = new Terser(m);
	    String pid31="XXX";
	    try {
		pid31 = t.get("PID-3-1");
		// Confirm that terser can set values into message
		t.set("PID-3-1","new");
		exchange.getIn().setHeader("PID31a",pid31);
		exchange.getIn().setBody(m);
		exchange.getIn().setHeader("PID31b",t.get("PID-3-1"));
	    } catch (Exception e) {
		System.err.println("Error getting pid31 from message body (hl7)");
	    }
	    /*
	    try {
		exchange.setProperty("CamelMllpAcknowledgementString",
				     "MSH|^~\\&|RS|RF|SA|SF|20250328140203.319-0700||ACK^A01|MSG00001A|P|2.5.1\rMSA|AA|MSG00001");
	    } catch (Exception e) {
		System.err.println("Error setting property CamelMllpAcknowledgementString");
		}
	    */
	    return ;
    }
}
