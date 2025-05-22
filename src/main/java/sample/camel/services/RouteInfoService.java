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
package sample.camel.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.CamelContext;
import org.apache.camel.api.management.ManagedCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteInfoService {

    @Autowired
    private CamelContext camelContext;
    public List<Map<String, List<String>>> getRouteDetailsAsMapIdToList() {
	    ManagedCamelContext managed = camelContext.getCamelContextExtension().getContextPlugin(ManagedCamelContext.class);

        return camelContext.getRoutes().stream().map(r -> {
            Map<String, List<String>> routeInfo = new HashMap<>();
            routeInfo.put(
                    r.getId(),
                    List.of(camelContext.getRouteController().getRouteStatus(r.getId()).name(),
			    r.getUptime(),
			    String.valueOf(managed.getManagedRoute(r.getId()).getExchangesCompleted()),
			    String.valueOf(managed.getManagedRoute(r.getId()).getExchangesFailed()),
			    String.valueOf(managed.getManagedRoute(r.getId()).getExchangesInflight())
			    /* String.valueOf(999L)*/
				   )
            );
            return routeInfo;
        }).collect(Collectors.toList());
    }
}
