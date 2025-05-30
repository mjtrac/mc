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
package sample.camel.services;

import java.io.InputStream;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.Resource;
import org.apache.camel.spi.RoutesLoader;
import org.apache.camel.support.PluginHelper;
import org.apache.camel.support.ResourceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteLoaderService {

    private static final Logger LOG = LoggerFactory.getLogger(RouteLoaderService.class);

    
    @Autowired
    private CamelContext camelContext;

    public void addRouteFromYaml(InputStream fileInputStream) throws Exception {
	RoutesLoader loader;
	byte[] buffer = new byte[1024];
        int bytesRead;
	String allContent = "";
	//camelContext.getRoutes().forEach(r -> System.out.println("Loaded route: " + r.getId()));
        // Read the file input stream buffer by buffer
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            // Convert the bytes read into a string (using UTF-8 encoding)
            String content = new String(buffer, 0, bytesRead, "UTF-8");
	    allContent = allContent + content;
        }
	
	loader = PluginHelper.getRoutesLoader(camelContext);

	loader.loadRoutes(setResource(allContent,"yaml"));
	// let generated exceptions be processed by caller
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

        switch (type) {
            case "xml":
                return ResourceHelper.fromString("route_" + uuid + ".xml", route);
            case "yaml":
                return ResourceHelper.fromString("route_" + uuid + ".yaml", route);
            default:
                System.out.println("unknown route format");
                break;
        }
	return null;
    }
}
	
