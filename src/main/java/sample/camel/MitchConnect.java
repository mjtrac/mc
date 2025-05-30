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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.springframework.scheduling.annotation.EnableScheduling;

//CHECKSTYLE:OFF
/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication
@EnableScheduling
public class MitchConnect {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MitchConnect.class, args);
    }

    @Bean
    public AtomicReference<SSLContextParameters> sslRef() {
	return new AtomicReference<>();
}
    
}
//CHECKSTYLE:ON
