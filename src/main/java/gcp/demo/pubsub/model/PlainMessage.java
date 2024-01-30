package gcp.demo.pubsub.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@NoArgsConstructor
@SuperBuilder
public class PlainMessage extends PubSubMessage {
    public PlainMessage(String orderingKey, Map<String, String> attributesMap, String gcpPubSubMessageId, String body, int receiptCount, PubSubMessageStatus ackStatus) {
        super(orderingKey, attributesMap, gcpPubSubMessageId, body, receiptCount, ackStatus);
    }

    public static PlainMessageBuilder copyFrom(PubSubMessage msg) {
        return PlainMessage.builder()
                .gcpPubSubMessageId(msg.getGcpPubSubMessageId())
                .receiptCount(msg.getReceiptCount())
                .body(msg.getBody())
                .attributesMap(msg.getAttributesMap())
                .orderingKey(msg.getOrderingKey())
                .ackStatus(msg.getAckStatus());
    }
}
