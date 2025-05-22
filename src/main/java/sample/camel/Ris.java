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

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.api.management.ManagedCamelContext;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Ris {

    private static final Logger logger = LoggerFactory.getLogger(Ris.class);

    private final CamelContext camelContext;

    public Ris(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public long getCompletedExchanges(Route r) {
        try {
	    ManagedCamelContext managed = camelContext.getCamelContextExtension().getContextPlugin(ManagedCamelContext.class);
	    ManagedRouteMBean routeMBean = managed.getManagedRoute(r.getId());
		/* Access the ManagementStrategy to retrieve route metrics
            ManagementStrategy managementStrategy = camelContext.getManagementStrategy();
            ManagementObjectStrategy objectStrategy = managementStrategy.getManagementObjectStrategy();
	    System.out.println(managementStrategy);
	    System.out.println(objectStrategy);
            // Retrieve the managed route MBean for the specific route
            ManagedRouteMBean routeMBean = (ManagedRouteMBean) objectStrategy
                    .getManagedObjectForRoute(camelContext, r);
	    */
            // Log the exception using a logger
            logger.error("An error occurred while retrieving route metrics");
	    System.out.println(routeMBean.getExchangesTotal());
            // Return the number of exchanges completed for the route
            return routeMBean.getExchangesTotal();
        } catch (Exception e) {

            return -1; // Handle any exceptions (log them in production)
        }
    }
}
