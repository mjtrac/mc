package sample.camel.services;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.camel.api.management.ManagedCamelContext;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.camel.spi.ManagementObjectStrategy;
import org.apache.camel.spi.ManagementStrategy;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Collections;

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
