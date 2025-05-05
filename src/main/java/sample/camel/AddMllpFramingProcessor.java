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

package sample.camel ;

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
