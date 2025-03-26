package sample.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class MllpJdbcInterceptorRoute extends RouteBuilder {

    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configure() throws Exception {
	getContext().getComponent("jdbc", org.apache.camel.component.jdbc.JdbcComponent.class).setDataSource(dataSource);
	interceptFrom("mllp:*")
	    .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
exchange.getContext().createProducerTemplate().sendBodyAndHeaders(
					       "direct:insert",
					       exchange.getIn().getBody(),
					       exchange.getIn().getHeaders()
					       );
                    }
                })
	    .log("MLLP message intercepted and JDBC route called.");
    }
}
