package sample.camel.db;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/db")
@Tag(name = "Route Message API")
public class RouteMessageApiController {

    private final RouteMessageService service;

    public RouteMessageApiController(RouteMessageService service) {
        this.service = service;
    }

    @GetMapping("/counts")
    public List<Map<String, Object>> getCounts() {
        List<Object[]> data = service.getCountPerRoute();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] entry : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("routeId", entry[0]);
            map.put("count", entry[1]);
            result.add(map);
        }
        return result;
    }

    @GetMapping("/{routeId}")
    public List<RouteMessage> getMessages(@PathVariable String routeId,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.getMessagesByRouteAndTime(routeId, from, to);
    }
}
