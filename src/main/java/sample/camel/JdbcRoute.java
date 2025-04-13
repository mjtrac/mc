package sample.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JdbcRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("direct:insert")
		.routeId("insertMessageToDB")
                .setBody(simple("INSERT INTO mytable (name, value) VALUES ('${header.name}', '${header.value}')"))
                .to("jdbc:dataSource");

            from("direct:select")
		.routeId("selectMessageFromDB")
                .setBody(simple("SELECT * FROM mytable WHERE name = '${header.name}'"))
                .to("jdbc:dataSource")
                .log("Query result: ${body}");

            from("direct:createTable")
		.routeId("createDB")
                .setBody(simple("CREATE TABLE IF NOT EXISTS mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), value VARCHAR(4096))"))
                .to("jdbc:dataSource");

            from("direct:init")
		.routeId("initRoute")
                .to("direct:createTable");
        }
    }
