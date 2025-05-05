package sample.camel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessageRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Your custom message after Spring Boot startup
        System.out.println("MitchConnect is now running.");
        System.out.println("For loaded routes, see http://localhost:8080/api/routes2");
        System.out.println("To experiment with route loading, use routes hello.yaml and hello2.yaml in src/main/resources/unloadedroutes");
	System.out.println("To modify ports for the existing routes,");
	System.out.println("edit src/main/resources/application.yaml and restart.");
    }
}
