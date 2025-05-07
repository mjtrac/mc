package sample.camel.ui;

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
import sample.camel.services.RouteInfoService;
import sample.camel.services.RouteLoaderService;
import sample.camel.services.SslUtils;
import sample.camel.services.SslUpdateRequest;

@Controller
@RequestMapping("/ui")
public class RouteUIController {

    private static final Logger LOG = LoggerFactory.getLogger(RouteUIController.class);
    
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ModelCamelContext modelCamelContext;

    @Autowired
    private AtomicReference<SSLContextParameters> sslRef;

    @Autowired
    private RouteLoaderService routeLoaderService; 

    @Autowired
    RouteInfoService routeInfoService;

    @GetMapping("/")
    public String listRoutesToUI(Model model) {
	    String contextId = camelContext.getName();
	    List<Map<String, List<String>>> routes = routeInfoService.getRouteDetailsAsMapIdToList();
	    model.addAttribute("routes", routes);
        return "routes";  // Points to src/main/resources/templates/routes.html
    }
	
    // List all routes using Thymeleaf
    @GetMapping("/routes")
    public String listRoutesWithT(Model model) {
	    String contextId = camelContext.getName();
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
	    e.printStackTrace();
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
