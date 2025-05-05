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
package sample.camel ;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.spi.RoutePolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EarlyCamelConfig {

    @Bean
    public RoutePolicyFactory loggingRoutePolicyFactory() {
	System.out.println("About to return new LoggingRoutePolicyFactory");
        return new LoggingRoutePolicyFactory();
    }

    @Bean
    public CamelContextConfiguration routePolicyRegistrar(RoutePolicyFactory factory) {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
		System.out.println("About to call addRoutePolicyFactory");
                context.addRoutePolicyFactory(factory);
            }

            @Override
            public void afterApplicationStart(CamelContext context) {
                // no-op
            }
        };
    }
}
