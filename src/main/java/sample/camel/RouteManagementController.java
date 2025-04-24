package sample.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.api.management.ManagedCamelContext;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.camel.api.management.mbean.ManagedCamelContextMBean;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.Resource;
import org.apache.camel.spi.RoutesBuilderLoader;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.ResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import java.lang.management.ManagementFactory;


@Controller
@RequestMapping("/api")
public class RouteManagementController {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ModelCamelContext modelCamelContext;

    @Autowired
    private AtomicReference<SSLContextParameters> sslRef;

   @Autowired
    private RouteLoaderService routeLoaderService; // Inject RouteLoaderService

    
    // List all routes
    @GetMapping("/routes")
    public List<Map<String, String>> listRoutes() {
	System.out.println("In listRoutes via /routes");
        return camelContext.getRoutes().stream().map(route -> Map.of(
            "id", route.getId(),
            "status", camelContext.getRouteController().getRouteStatus(route.getId()).name()
        )).collect(Collectors.toList());
    }

    // List all routes using Thymeleaf
    @GetMapping("/routes2")
    public String listRoutesWithT(Model model) {
	    String contextId = camelContext.getName();

	    List<Map<String, String>> routes = camelContext.getRoutes().stream().map(
	    r -> Map.of(
			"id", r.getId(),// + " " + "Tot/Fail/Inflight 0/0/0",
			"status", camelContext.getRouteController().getRouteStatus(r.getId()).name()
			)
        ).collect(Collectors.toList());
	model.addAttribute("routes", routes);
        return "routes";  // Points to src/main/resources/templates/routes.html
    }

    // Start a specific route
    @PostMapping("/routes2/{routeId}/start")
    public RedirectView startRoute(@PathVariable("routeId") String routeId, Model model) {
        try {
            camelContext.getRouteController().startRoute(routeId);
            return new RedirectView("/api/routes2");//ResponseEntity.ok("Route stopped: " + routeId);
        } catch (Exception e) {
            return new RedirectView("/api/routes2");//ResponseEntity.status(500).body("Failed to stop route: " + e.getMessage());
        }
    }

    // Stop a specific route
    @PostMapping("/routes2/{routeId}/stop")
    public RedirectView stopRoute(@PathVariable("routeId") String routeId, Model model) {
	System.out.println("In stopRoute via /routes/.../stop");
        try {
            camelContext.getRouteController().stopRoute(routeId);
            return new RedirectView("/api/routes2");//ResponseEntity.ok("Route stopped: " + routeId);
        } catch (Exception e) {
            return new RedirectView("/api/routes2");//ResponseEntity.status(500).body("Failed to stop route: " + e.getMessage());
        }
    }

    // Dynamically load a YAML route
    @GetMapping("/routes2/load")
    public String showRouteForm(Model model) {
	return "route-form";
    }
    
    @PostMapping("/routes2/loadtext")
    public RedirectView loadRouteText(@RequestParam("yaml_content") String yamlContent) {
	System.out.println(yamlContent);

	
	try (InputStream is = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8))) {
	    routeLoaderService.addRouteFromYaml(is);
            return new RedirectView("/api/routes2");
	} catch (Exception e) {
	    System.out.println("Trouble");
	    return new RedirectView("/api/routes2/load");
	}
	
    }

    @PostMapping("/routes2/loadfile")
    public RedirectView loadRouteFile(@RequestParam("yamlFile") MultipartFile file, Model model)  {
	System.out.println(model);
	try {
	    InputStream fileInputStream = file.getInputStream();
	    routeLoaderService.addRouteFromYaml(fileInputStream);
	    return new RedirectView("/api/routes2");
	} catch (Exception e) {
	    e.printStackTrace();
	    return new RedirectView("/api/routes2/load");
	    
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
