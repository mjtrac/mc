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
