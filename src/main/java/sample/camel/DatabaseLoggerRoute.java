package sample.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoggerRoute extends RouteBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate; // Autowire JdbcTemplate
    @Override
    public void configure() throws Exception {
        // This route is used by RoutePolicy for logging to the database
        from("direct:logToDatabase")
            .routeId("logToDatabase")
            .process(exchange -> {
		    // Prepare the SQL statement
		    // with positional placeholders (?)
		    // to accomodate SQLite
		    String sql = "INSERT INTO mytable (name, value, route) VALUES (?, ?, ?)";
                
		    // the body becomes the value for database
		    String value = exchange.getIn().getBody(String.class);
		    String routeId = (String)(exchange.getIn().getHeader("originalRouteId"));
		    // Execute the SQL query using JdbcTemplate
		    jdbcTemplate.update(sql, "body", value, routeId);
		});
    }
}
