package sample.camel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

@Controller
public class RouteWebController {

    @Autowired
    private RouteLoaderService routeLoaderService;

    // Serve the form to the user
    @GetMapping("/loadroute")
    public String showRouteForm(Model model) {
        return "route-form"; // This will return a view named "route-form"
    }

    // Handle the form submission and load the route
    @PostMapping("/loadroute")
    public String loadRoute(@RequestParam("yamlFile") MultipartFile file, Model model) {
        try {
            // Try to load the route based on the provided path
          InputStream fileInputStream = file.getInputStream();
	  routeLoaderService.addRouteFromYaml(fileInputStream);

            // Add a success message to the model
            model.addAttribute("message", "Route loaded successfully!");
            return "route-form"; // Return the same form page with the success message
        } catch (Exception e) {
            // In case of an error, add the error message
            model.addAttribute("message", "Error loading route: " + e.getMessage());
            return "route-form"; // Return the same form page with the error message
        }
    }
}
