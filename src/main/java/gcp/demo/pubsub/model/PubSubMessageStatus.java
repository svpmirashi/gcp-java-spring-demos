package gcp.demo.pubsub.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum PubSubMessageStatus {
    OUTSTANDING ("OUTSTANDING"),
    ACK ("ACK"),
    NACK ("NACK");

    private String status;

    PubSubMessageStatus(String status) {
        this.status = status;
    }
}
