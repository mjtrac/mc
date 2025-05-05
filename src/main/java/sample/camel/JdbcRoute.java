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
import org.springframework.stereotype.Component;

@Component
public class JdbcRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("direct:select")
		.routeId("selectMessageFromDB")
                .setBody(simple("SELECT * FROM mytable WHERE name = '${header.name}'"))
                .to("jdbc:dataSource")
                .log("Query result: ${body}");

            from("direct:createTable")
		.routeId("createDB")
                .setBody(simple("CREATE TABLE IF NOT EXISTS mytable (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(255),value VARCHAR(4096),created_at DATETIME DEFAULT CURRENT_TIMESTAMP,route VARCHAR(255)"))
		.to("jdbc:dataSource");

            from("direct:init")
		.routeId("initRoute")
                .to("direct:createTable");
        }
    }
