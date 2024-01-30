package gcp.demo.pubsub.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RmsMessage extends PubSubMessage {
    private static Logger LOGGER = Logger.getLogger(RmsMessage.class.getName());
    private TradeIdVersion tradeIdVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RmsMessage that)) return false;
        return Objects.equals(tradeIdVersion, that.tradeIdVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeIdVersion);
    }

    public static RmsMessageBuilder copyFrom(PubSubMessage msg) {
        return RmsMessage.builder()
        .gcpPubSubMessageId(msg.getGcpPubSubMessageId())
        .receiptCount(msg.getReceiptCount())
        .body(msg.getBody())
        .attributesMap(msg.getAttributesMap())
        .orderingKey(msg.getOrderingKey())
        .ackStatus(msg.getAckStatus());
    }

    @Override
    public void display() {
        StringBuilder log = new StringBuilder("RmsMessage [");
        log.append("Message_ID: ");
        log.append(getGcpPubSubMessageId());
        log.append(",TradeIdVersion: ");
        log.append(getTradeIdVersion().toString());
        log.append(", Ordering_Key: ");
        log.append(getOrderingKey() == null || getOrderingKey().isEmpty()? "None" : getOrderingKey());
        log.append(", Attributes: ");
        log.append(getAttributesMap() == null ? "None" : getAttributesMap());
        log.append(",Body: ");
        log.append(getBody());
        log.append(",Count: ");
        log.append(getReceiptCount());
        log.append(",Ack_Status: ");
        log.append(getAckStatus());
        log.append("]");
        LOGGER.info(log.toString());
    }
}
