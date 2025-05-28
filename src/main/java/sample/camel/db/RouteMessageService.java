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
package sample.camel.db;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

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

    public void logCurrentBody(Exchange exchange, String additional_id) {
	RouteMessage entity = new RouteMessage();
	entity.setRouteId(exchange.getFromRouteId());
	entity.setExchangeId(exchange.getExchangeId());
	entity.setBody(exchange.getIn().getBody(String.class));
	entity.setAdditionalId(additional_id);
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
