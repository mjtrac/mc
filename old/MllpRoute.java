package sample.camel;

import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.netty.NettyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import sample.camel.MllpSslConfig;

@Component
public class MllpRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
	from("mllp://127.0.0.1:15680?port=15680&sslContextParameters=#sslContextParameters")
	    .routeId("mllpSslRoute")
	    .to("log:myLog");
	    
    }
}
