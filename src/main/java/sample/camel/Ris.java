package sample.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.api.management.ManagedCamelContext;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.camel.spi.ManagementObjectStrategy;
import org.apache.camel.spi.ManagementStrategy;
import org.springframework.stereotype.Service;

@Service
public class Ris {

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
	    System.out.println(routeMBean);
	    System.out.println(routeMBean.getExchangesTotal());
            if (routeMBean != null) {
                // Return the number of exchanges completed for the route
                return routeMBean.getExchangesTotal();
            } else {
                // If the MBean is null, something went wrong
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Handle any exceptions (log them in production)
        }
    }
}
