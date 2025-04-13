package sample.camel;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.apache.camel.Exchange;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.RouteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class RouteControllerBean {

    private final RouteController routeController;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    public RouteControllerBean(CamelContext camelContext) {
        this.routeController = camelContext.getRouteController();
    }

    public void startRoute(Exchange exchange) throws Exception {
        // Replace "x" with the actual ID of the route you want to start
        routeController.startRoute("encompassingMllpRoute");
	// Set the HTTP response code and body after starting the route
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);  
        exchange.getIn().setBody("Route successfully started");        

    }

    public void stopRoute(Exchange exchange) throws Exception {
        // Replace "x" with the actual ID of the route you want to stop
        routeController.stopRoute("encompassingMllpRoute");
	// Set the HTTP response code and body after starting the route
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);  
        exchange.getIn().setBody("Route successfully stopped");        

    }

    public void loadRoute(Exchange exchange) throws Exception {
	// Set the HTTP response code and body after starting the route
	MultipartFile f = exchange.getIn().getHeader("yamlFile",MultipartFile.class);
	System.out.println(f);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);  
        exchange.getIn().setBody("Route loading not under /api");        

    }

    // Get a list of all routes
    public void  listRoutes(Exchange exchange) {
	    exchange.getMessage().setBody("{'text':'"
					  + camelContext.getRoutes().stream()
					  .map(Route::getId) 
					  .collect(Collectors.toList())
						   + "'}");
    }
    public void listRouteDefinitions(Exchange exchange) {
	ModelCamelContext modelContext = (ModelCamelContext)camelContext;
	RouteDefinition routeDefinition = modelContext.getRouteDefinition("encompassingMllpRoute");

	if (routeDefinition != null) {
	    exchange.getIn().setBody(routeDefinition.toString());  // Convert to a readable format
	} else {
	    exchange.getIn().setBody( "Route not found");
	}
    }

}


