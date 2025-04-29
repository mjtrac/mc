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
