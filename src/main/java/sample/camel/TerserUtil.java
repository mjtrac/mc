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
