/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import java.util.List;
import java.util.ArrayList;
import org.apache.camel.Exchange;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.RouteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

/**
 * A simple Camel REST DSL route with OpenApi API documentation.
 */
@Component
public class CamelRouter extends RouteBuilder {
    
    @Autowired
    private Environment env;

    private final CamelContext camelContext;


    @Autowired
    public CamelRouter(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    @Value("/api/*")
    private String contextPath ;

    @Override
    public void configure() throws Exception {

        // @formatter:off
        RouteController routeController = camelContext.getRouteController();
        // this can also be configured in application.properties
	//0, contextPath.length() - 2))
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .enableCORS(true)
            .port(env.getProperty("server.port", "8080"))
            // turn on openapi api-doc
	    //.contextPath(contextPath.substring(0, contextPath.length() - 2))
            // turn on openapi api-doc
	    .contextPath("/api")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "User API")
            .apiProperty("api.version", "1.0.0");

	// List Routes
	rest("/listroutes")
	    .get()
	    .routeId("listroutes")
	    .description("List all routes")
	    .produces("application/json")
	    .outType(List.class)
	    .responseMessage()
	    .code(200)
	    .message("Routes listed")
	    .endResponseMessage()
	    .to("bean:routeControllerBean?method=listRoutes");

	rest("/listroutedefs")
	    .get()
	    .description("For now, just get the mllp route.")
	    .outType(String.class)  // Define the response type
	    .to("bean:routeControllerBean?method=listRouteDefinitions");


	// Start MLLP route
        rest("/startmllp")
            .get()
	    .routeId("startMLLP")
            .description("Start MLLP Route")
	    .responseMessage()
	    .code(200)
	    .message("Route started")
	    .endResponseMessage()
            .to("bean:routeControllerBean?method=startRoute");

        // Stop MLLP route
        rest("/stopmllp")
            .get()
            .description("Stop MLLP Route")
	    .routeId("stopMLLP")
	    .responseMessage()
	    .code(200)
	    .message("Route stopped")
	    .endResponseMessage()
            .to("bean:routeControllerBean?method=stopRoute");

    
        // Load route from yaml file named in form
        rest("/loadroute")
            .get()
            .description("Load Route chosen from form")
	    .responseMessage()
	    .code(200)
	    .message("Route loaded")
	    .endResponseMessage()
            .to("bean:routeControllerBean?method=showRouteForm");

    
        rest("/users").description("User REST service")
            .consumes("application/json")
            .produces("application/json")

            .get().routeId("listusers").description("Find all users").outType(User[].class)
                .responseMessage().code(200).message("All users successfully returned").endResponseMessage()
                .to("bean:userService?method=findUsers")
        
            .get("/{id}").routeId("getUser").description("Find user by ID")
                .outType(User.class)
                .param().name("id").type(path).description("The ID of the user").dataType("integer").endParam()
                .responseMessage().code(200).message("User successfully returned").endResponseMessage()
                .to("bean:userService?method=findUser(${header.id})")

            .put("/{id}").routeId("putUser").description("Update a user").type(User.class)
                .param().name("id").type(path).description("The ID of the user to update").dataType("integer").endParam()    
                .param().name("body").type(body).description("The user to update").endParam()
                .responseMessage().code(204).message("User successfully updated").endResponseMessage()
                .to("direct:update-user");
        
        from("direct:update-user")
            .to("bean:userService?method=updateUser")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
            .setBody(constant(""));

        // @formatter:on
    }


}
