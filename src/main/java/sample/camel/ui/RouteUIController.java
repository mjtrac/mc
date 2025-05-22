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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sample.camel.services.RouteInfoService;
import sample.camel.services.RouteLoaderService;
import sample.camel.services.SslUpdateRequest;
import sample.camel.services.SslUtils;

@Controller
@RequestMapping("/ui")
public class RouteUIController {

    private static final Logger LOG = LoggerFactory.getLogger(RouteUIController.class);
    
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private AtomicReference<SSLContextParameters> sslRef;

    @Autowired
    private RouteLoaderService routeLoaderService; 

    @Autowired
    RouteInfoService routeInfoService;

    @GetMapping("/")
    public String listRoutesToUI(Model model) {
	    List<Map<String, List<String>>> routes = routeInfoService.getRouteDetailsAsMapIdToList();
	    model.addAttribute("routes", routes);
        return "routes";  // Points to src/main/resources/templates/routes.html
    }
	
    // List all routes using Thymeleaf
    @GetMapping("/routes")
    public String listRoutesWithT(Model model) {
	    List<Map<String, List<String>>> routes = routeInfoService.getRouteDetailsAsMapIdToList();
	    model.addAttribute("routes", routes);
        return "routes";  // Points to src/main/resources/templates/routes.html
    }

    // Start a specific route
    @PostMapping("/routes/{routeId}/start")
    public String startRoute(@PathVariable("routeId") String routeId, RedirectAttributes redirectAttributes){
	LOG.info("In startRoute via /routes/.../start");
        try {
            camelContext.getRouteController().startRoute(routeId);
	    redirectAttributes.addFlashAttribute("message", "Started route: " + routeId);
        } catch (Exception e) {
	    redirectAttributes.addFlashAttribute("error", "Failed to start route: " + e.getMessage());
        }
            return "redirect:/ui/routes";
    }

    // Stop a specific route
    @PostMapping("/routes/{routeId}/stop")
    public String stopRoute(@PathVariable("routeId") String routeId, RedirectAttributes redirectAttributes){
	LOG.info("In stopRoute via /routes/.../stop");
        try {
            camelContext.getRouteController().stopRoute(routeId);
	    redirectAttributes.addFlashAttribute("message", "Stopped route: " + routeId);
        } catch (Exception e) {
	    redirectAttributes.addFlashAttribute("error", "Failed to stop route: " + e.getMessage());
        }
            return "redirect:/ui/routes";
    }

    // Suspend a specific route
    @PostMapping("/routes/{routeId}/suspend")
    public String suspendRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In suspendRoute via /routes/.../suspend");
        try {
            camelContext.getRouteController().suspendRoute(routeId);
            return "redirect:/ui/routes";
        } catch (Exception e) {
            return "redirect:/ui/routes";
        }
    }

    // Remove a specific route
    @PostMapping("/routes/{routeId}/remove")
    public String removeRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.info("In removeRoute via /routes/.../remove");
        try {
            camelContext.getRouteController().stopRoute(routeId);
            camelContext.removeRoute(routeId);
            return "redirect:/ui/routes";
        } catch (Exception e) {
            return "redirect:/ui/routes";
        }
    }

    // Dynamically load a YAML route
    @GetMapping("/routes/load")
    public String showRouteForm(Model model) {
	return "route-form";
    }
    
    @PostMapping("/routes/loadtext")
    public String loadRouteText(@RequestParam("yaml_content") String yamlContent, RedirectAttributes redirectAttributes) {
	try (InputStream is = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8))) {
	    routeLoaderService.addRouteFromYaml(is);
	    LOG.info("RouteLoaderService no exception");
            return "redirect:/ui/routes";
	} catch (Exception e) {
	    //LOG(e.printStackTrace());
	    //LOG.error(e);
	    redirectAttributes.addFlashAttribute("error",e.getMessage());
	    LOG.warn("RouteLoaderService " + e.getMessage());
	    return "redirect:/ui/routes";
	}
	
    }

    @PostMapping("/routes/loadfile")
    public String loadRouteFile(@RequestParam("yamlFile") MultipartFile file, Model model)  {
	try {
	    InputStream fileInputStream = file.getInputStream();
	    routeLoaderService.addRouteFromYaml(fileInputStream);
	    return "redirect:/ui/routes";
	} catch (Exception e) {
	
	    return "redirect:/ui/routes";
	    
	    }
    }

    
    // Update SSLContextParameters at runtime
    @PostMapping("/ssl/update")
    public ResponseEntity<String> updateSSL(@RequestBody SslUpdateRequest request) {
        try {
            SSLContextParameters newParams = SslUtils.buildSslContextParams(request);
            sslRef.set(newParams);
            return ResponseEntity.ok("SSL context updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update SSL context: " + e.getMessage());
        }
    }
}
