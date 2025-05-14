package sample.camel.db;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_messages")
public class RouteMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String routeId;
    private String exchangeId;
    private LocalDateTime timestampProcessed;

    @Column(length = 80)
    private String additionalId;

    @Column(length = 1000)
    private String comment;

    @Lob
    @Column(length = 5 * 1024 * 1024)
    private String body;

    // Getters and setters, lombok trouble
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public LocalDateTime getTimestampProcessed() {
        return timestampProcessed;
    }

    public void setTimestampProcessed(LocalDateTime timestampProcessed) {
        this.timestampProcessed = timestampProcessed;
    }

    public String getAdditionalId() {
        return additionalId;
    }

    public void setAdditionalId(String additionalId) {
        this.additionalId = additionalId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
