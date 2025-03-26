package sample.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JdbcRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("direct:insert")
                .setBody(simple("INSERT INTO mytable (name, value) VALUES ('${header.name}', '${header.value}')"))
                .to("jdbc:dataSource");

            from("direct:select")
                .setBody(simple("SELECT * FROM mytable WHERE name = '${header.name}'"))
                .to("jdbc:dataSource")
                .log("Query result: ${body}");

            from("direct:createTable")
                .setBody(simple("CREATE TABLE IF NOT EXISTS mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), value VARCHAR(4096))"))
                .to("jdbc:dataSource");

            from("direct:init")
                .to("direct:createTable");
        }
    }
