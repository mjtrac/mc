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

package sample.camel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessageRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Your custom message after Spring Boot startup
        System.out.println("MitchConnect is now running.");
        System.out.println("For loaded routes, see http://localhost:8080/ui/routes");
	System.out.println("User: admin  password: password");
	System.out.println("API at http://localhost:8080/swagger-ui/index.html");
        System.out.println("To experiment with route loading, use routes hello.yaml and hello2.yaml in src/main/resources/unloadedroutes");
	System.out.println("To modify ports for the existing routes,");
	System.out.println("edit src/main/resources/application.yaml and restart.");
    }
}
