package sample.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.RoutesLoader;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.PluginHelper;
import org.apache.camel.support.ResourceHelper;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import org.apache.commons.io.input.TeeInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.IOException;

@Service
public class RouteLoaderService {

    private static final Logger LOG = LoggerFactory.getLogger(RouteLoaderService.class);

    
    @Autowired
    private CamelContext camelContext;

    public void addRouteFromYaml(InputStream fileInputStream) throws Exception {
	RoutesLoader loader;
	Resource resource;
	Route route;
	byte[] buffer = new byte[1024];
        int bytesRead;
	String allContent = new String("");
	//camelContext.getRoutes().forEach(r -> System.out.println("Loaded route: " + r.getId()));
        // Read the file input stream buffer by buffer
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            // Convert the bytes read into a string (using UTF-8 encoding)
            String content = new String(buffer, 0, bytesRead, "UTF-8");
	    allContent = allContent + content;
        }
	
	loader = PluginHelper.getRoutesLoader(camelContext);

	try {
	    loader.loadRoutes(setResource(allContent,"yaml"));
	    LOG.error(loader.toString());
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	}
        /*
	  System.out.println("File Content:\n" + allContent);
	camelContext.getRoutes().forEach(r -> System.out.println("Loaded route: " + r.getId()));

	  Loading a route using Java DSL
	camelContext.addRoutes(new RouteBuilder() {
		@Override
		public void configure() throws Exception {
		    from("timer:foo?period=1000")
			.to("log:loadedRoute");
		}
	    });
	*/
    }

    private Resource setResource(String route, String type){

	String uuid = UUID.randomUUID().toString();

	if(type.equals("xml")) {
	    return ResourceHelper.fromString("route_" + uuid + ".xml", route);
	} else if(type.equals("yaml")) {
	    return ResourceHelper.fromString("route_" + uuid + ".yaml", route);
	} else {
	    System.out.println("unknown route format");
	}
	return null;
    }
}
	
