package sample.camel.db;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.camel.Exchange;
import sample.camel.db.RouteMessage;

@Service
public class RouteMessageService {

    private final RouteMessageRepository repo;

    public RouteMessageService(RouteMessageRepository repo) {
        this.repo = repo;
    }

    public void logRouteMessage(String routeId, String body, String exchangeId) {
	RouteMessage entity = new RouteMessage();
	entity.setRouteId(routeId);
	entity.setExchangeId(exchangeId);
	entity.setBody(body);
	entity.setTimestampProcessed(LocalDateTime.now());
	repo.save(entity);	
    }

    public void logCurrentBody(Exchange exchange) {
	RouteMessage entity = new RouteMessage();
	entity.setRouteId(exchange.getFromRouteId());
	entity.setExchangeId(exchange.getExchangeId());
	entity.setBody(exchange.getIn().getBody(String.class));
	entity.setTimestampProcessed(LocalDateTime.now());
	repo.save(entity);	
    }

    public List<Object[]> getCountPerRoute() {
        return repo.countMessagesByRoute();
    }

    public List<RouteMessage> getMessagesByRouteAndTime(String routeId, LocalDateTime from, LocalDateTime to) {
        return repo.findByRouteIdAndTimestampProcessedBetween(routeId, from, to);
    }
}
