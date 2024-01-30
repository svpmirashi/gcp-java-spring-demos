package gcp.demo.pubsub.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class PubSubMessage {
    private static Logger LOGGER = Logger.getLogger(PubSubMessage.class.getName());

    private String orderingKey;
    private Map<String, String> attributesMap;
    private String gcpPubSubMessageId;
    private String body;
    private int receiptCount;

    private PubSubMessageStatus ackStatus;
    public void display() {
        StringBuilder log = new StringBuilder("PubSubMessage [");
        log.append("Message_ID: ");
        log.append(getGcpPubSubMessageId());
        log.append(", Ordering_Key: ");
        log.append(getOrderingKey() == null || getOrderingKey().isEmpty()? "None" : getOrderingKey());
        log.append(", Attributes: ");
        log.append(getAttributesMap() == null ? "None" : getAttributesMap());
        log.append(", Body: ");
        log.append(getBody());
        log.append(", Count: ");
        log.append(getReceiptCount());
        log.append(", Ack_Status: ");
        log.append(getAckStatus());
        log.append("]");
        LOGGER.info(log.toString());
    }

}
