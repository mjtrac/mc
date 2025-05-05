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
