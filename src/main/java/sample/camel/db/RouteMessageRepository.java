package sample.camel.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface RouteMessageRepository extends JpaRepository<RouteMessage, Long> {

    @Query("SELECT rm.routeId, COUNT(rm) FROM RouteMessage rm GROUP BY rm.routeId")
    List<Object[]> countMessagesByRoute();

    List<RouteMessage> findByRouteIdAndTimestampProcessedBetween(String routeId, LocalDateTime start, LocalDateTime end);
}
