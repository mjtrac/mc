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
