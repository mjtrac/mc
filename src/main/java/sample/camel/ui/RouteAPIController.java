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
package sample.camel.ui;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.CamelContext;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sample.camel.services.RouteInfoService;
import sample.camel.services.RouteLoaderService;

@RestController
@RequestMapping("/api")
public class RouteAPIController {

    private static final Logger LOG = LoggerFactory.getLogger(RouteAPIController.class);
    
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private AtomicReference<SSLContextParameters> sslRef;

    @Autowired
    private RouteLoaderService routeLoaderService; // Inject RouteLoaderService

    @Autowired RouteInfoService routeInfoService;

    // List all routes for API
    @GetMapping("/routes2")
    public List<Map<String, List<String>>> listRoutes() {
	String contextId = camelContext.getName();
	return routeInfoService.getRouteDetailsAsMapIdToList();
    }

    // Start a specific route
    @PostMapping("/routes2/{routeId}/start")
    public String startRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In startRoute via /routes2/.../start");
        try {
            camelContext.getRouteController().startRoute(routeId);
            return "Started " + routeId;
        } catch (Exception e) {
            return "Failed to start" + routeId + ": " + e.getMessage();
        }
    }

    // Stop a specific route
    @PostMapping("/routes2/{routeId}/stop")
    public String stopRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In stopRoute via /routes/.../stop");
        try {
            camelContext.getRouteController().stopRoute(routeId);
            return "Stopped " + routeId;
        } catch (Exception e) {
            return "Failed to stop " + routeId + ": " + e.getMessage(); 
        }
    }

    // Suspend a specific route
    @PostMapping("/routes2/{routeId}/suspend")
    public String suspendRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In suspendRoute via /routes/.../suspend");
        try {
            camelContext.getRouteController().suspendRoute(routeId);
            return "Suspended " + routeId;
        } catch (Exception e) {
            return "Failed to suspend " + routeId + ": " + e.getMessage();
        }
    }
    
    // Suspend a specific route
    @PostMapping("/routes2/{routeId}/remove")
    public String removeRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In removeRoute via /routes/.../remove");
        try {
            camelContext.removeRoute(routeId);
            return "Removed " + routeId;
        } catch (Exception e) {
            return "Failed to remove " + routeId + ": " + e.getMessage();
        }
    }
    
    @PostMapping("/routes2/loadtext")
    public String loadRouteText(String yamlContent) {
	try (InputStream is = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8))) {
	    routeLoaderService.addRouteFromYaml(is);
            return "Loaded route:\n" + yamlContent;
	} catch (Exception e) {
	    return "Could not load route: \n" + yamlContent; 
	}
	
    }

    @PostMapping("/routes2/loadfile")
    public String loadRouteFile(String filePath)  {
	try {
	    InputStream fileInputStream = new FileInputStream(filePath);
	    routeLoaderService.addRouteFromYaml(fileInputStream);
	    return "Loaded route from " + filePath;
	} catch (Exception e) {
	    return "Could not load route from " + filePath + ": " + e.getMessage();
	    
	    }
    }

}
