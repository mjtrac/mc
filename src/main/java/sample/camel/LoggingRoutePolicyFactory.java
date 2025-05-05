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

import org.apache.camel.*;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.spi.RoutePolicyFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
//@Component
public class LoggingRoutePolicyFactory implements RoutePolicyFactory {

    @Autowired
    private ProducerTemplate producerTemplate;


    @Override
    public RoutePolicy createRoutePolicy(CamelContext camelContext, String routeId, NamedNode definition) {
        if (routeId != null && routeId != "logToDatabase") {
            return new RoutePolicy() {

		@Override
		public void onExchangeBegin(Route route, Exchange exchange) {
		    String body = exchange.getIn().getBody(String.class);
		    
		producerTemplate.sendBodyAndHeaders(
		       "direct:logToDatabase",
		       body,
		       Map.of("originalRouteId", (String)(route.getId())));
		}

                @Override public void onInit(Route route) {}
                @Override public void onRemove(Route route) {}
                @Override public void onStart(Route route) {}
                @Override public void onStop(Route route) {}
                @Override public void onSuspend(Route route) {}
                @Override public void onResume(Route route) {}
                // no longer available
		//@Override public void onExchangeSent(Route route, Exchange exchange, Endpoint endpoint, long timeTaken) {}

		@Override
		public void onExchangeDone(Route route, Exchange exchange) {
		    String body = exchange.getIn().getBody(String.class);
		    producerTemplate.sendBodyAndHeaders(
		       "direct:logToDatabase",
		       body,
		       Map.of("originalRouteId", (String)(route.getId())));
		}

	    };
        }
        return null;
    }
}
