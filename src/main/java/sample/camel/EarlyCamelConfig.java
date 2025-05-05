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
