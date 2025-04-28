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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    private static final Logger LOG = LoggerFactory.getLogger(RouteManagementController.class);
    
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ModelCamelContext modelCamelContext;

    @Autowired
    private AtomicReference<SSLContextParameters> sslRef;

    @Autowired
    private RouteLoaderService routeLoaderService; // Inject RouteLoaderService

    @Autowired RouteInfoService routeInfoService;

    @Autowired Ris ris;
    
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
	    List<Map<String, List<String>>> routes2 = routeInfoService.getRouteDetailsAsMapIdToList();
	    model.addAttribute("routes2", routes2);
        return "routes2";  // Points to src/main/resources/templates/routes.html
    }

    // Start a specific route
    @PostMapping("/routes2/{routeId}/start")
    public RedirectView startRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.error("In startRoute via /routes/.../start");
        try {
            camelContext.getRouteController().startRoute(routeId);
            return new RedirectView("/api/routes2");//ResponseEntity.ok("Route started: " + routeId);
        } catch (Exception e) {
            return new RedirectView("/api/routes2");//ResponseEntity.status(500).body("Failed to stop route: " + e.getMessage());
        }
    }

    // Stop a specific route
    @PostMapping("/routes2/{routeId}/stop")
    public RedirectView stopRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.error("In stopRoute via /routes/.../stop");
	LOG.warn(String.valueOf(ris.getCompletedExchanges(camelContext.getRoute(routeId))));
        try {
            camelContext.getRouteController().stopRoute(routeId);
            return new RedirectView("/api/routes2");//ResponseEntity.ok("Route stopped: " + routeId);
        } catch (Exception e) {
            return new RedirectView("/api/routes2");//ResponseEntity.status(500).body("Failed to stop route: " + e.getMessage());
        }
    }

    // Suspend a specific route
    @PostMapping("/routes2/{routeId}/suspend")
    public RedirectView suspendRoute(@PathVariable("routeId") String routeId, Model model) {
	LOG.error("In stopRoute via /routes/.../suspend");
        try {
            camelContext.getRouteController().suspendRoute(routeId);
            return new RedirectView("/api/routes2");//ResponseEntity.ok("Route suspended: " + routeId);
        } catch (Exception e) {
            return new RedirectView("/api/routes2");//ResponseEntity.status(500).body("Failed to suspend route: " + e.getMessage());
        }
    }

    // Dynamically load a YAML route
    @GetMapping("/routes2/load")
    public String showRouteForm(Model model) {
	return "route-form";
    }
    
    @PostMapping("/routes2/loadtext")
    public RedirectView loadRouteText(@RequestParam("yaml_content") String yamlContent, RedirectAttributes redirectAttributes) {
	try (InputStream is = new ByteArrayInputStream(yamlContent.getBytes(StandardCharsets.UTF_8))) {
	    routeLoaderService.addRouteFromYaml(is);
	    LOG.error("RouteLoaderService no exception");
            return new RedirectView("/api/routes2");
	} catch (Exception e) {
	    //LOG(e.printStackTrace());
	    //LOG.error(e);
	    redirectAttributes.addFlashAttribute("error",e.getMessage());
	    LOG.error("RouteLoaderService " + e.getMessage());
	    return new RedirectView("/api/routes2/loadtext");
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
