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
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GlobalLogger extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        intercept().process(exchange -> {
		String routeId = "routeid";
            String exchangeId = exchange.getExchangeId();
            String fromEndpointUri = exchange.getFromEndpoint() != null ?
                                     exchange.getFromEndpoint().getEndpointUri() : "N/A";
            Object body = exchange.getIn().getBody();

            log.info("Global Interceptor - Route: '{}', Exchange ID: '{}', From: '{}', Body: '{}'",
                     routeId, exchangeId, fromEndpointUri, body);

            // You can perform other global actions here,
            // such as setting headers, modifying the body, etc.
        });
    }
}
