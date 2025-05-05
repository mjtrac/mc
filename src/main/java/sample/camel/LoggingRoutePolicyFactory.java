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
