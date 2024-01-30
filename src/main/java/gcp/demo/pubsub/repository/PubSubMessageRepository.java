package gcp.demo.pubsub.repository;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import gcp.demo.pubsub.model.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class PubSubMessageRepository {
    private static Map<TradeIdVersion, RmsMessage> rmsMessageCache = new HashMap<>();
    private static Map<String, PlainMessage> plainMessageCache = new HashMap<>();

    public PubSubMessage save(BasicAcknowledgeablePubsubMessage message) {
        TradeIdVersion key = buildTradeIdVersionObject(message);

        if (key == null) {
            String msgId = message.getPubsubMessage().getMessageId();

            plainMessageCache.computeIfPresent(msgId, (k,v) -> {
                v.setReceiptCount(v.getReceiptCount()+1);
                return v;
            });

            PlainMessage pm = buildPlainMessage(message);
            plainMessageCache.put(msgId, pm);
            return pm;
        }
        if(rmsMessageCache.containsKey(key)){
            rmsMessageCache.computeIfPresent(key, (k, v) -> {
                v.setReceiptCount(v.getReceiptCount()+1);
                return v;
            });
        }
        else {
            RmsMessage rmsMsg = buildRmsMessage(message);
            rmsMessageCache.put(key, rmsMsg);
            return rmsMsg;
        }
        return null;
    }

    public PubSubMessage getById(TradeIdVersion tradeIdVersion) {
        return getById(tradeIdVersion, null);
    }

    public PubSubMessage getById(TradeIdVersion tradeIdVersion, String messageId) {
        if(tradeIdVersion != null) {
            return rmsMessageCache.getOrDefault(tradeIdVersion, null);
        }
        if(messageId != null) {
            return plainMessageCache.getOrDefault(messageId, null);
        }

        return null;
    }

    private static TradeIdVersion buildTradeIdVersionObject(BasicAcknowledgeablePubsubMessage message) {
        AtomicReference<Optional<String>> tradeId = new AtomicReference<>();
        AtomicReference<Optional<String>> tradeVersion = new AtomicReference<>();

        message.getPubsubMessage()
                .getAttributesMap()
                .forEach((k,v) -> {
                    if(k.toLowerCase().equals(TradeIdVersion.TRADE_ID)) {
                        tradeId.set(Optional.of(v));
                    }
                    if(k.toLowerCase().equals(TradeIdVersion.TRADE_VERSION)) {
                        tradeVersion.set(Optional.of(v));
                    }
                });
        if (tradeId.get() == null || tradeId.get().isEmpty() || tradeVersion.get() == null || tradeVersion.get().isEmpty()) {
            return null;
        }
        return TradeIdVersion.builder()
                .tradeId(tradeId.get().get())
                .tradeVersion(tradeVersion.get().get())
                .build();
    }

    private static PlainMessage buildPlainMessage(BasicAcknowledgeablePubsubMessage message) {
        return PlainMessage.builder()
                .orderingKey(message.getPubsubMessage().getOrderingKey())
                .attributesMap(message.getPubsubMessage().getAttributesMap())
                .gcpPubSubMessageId(message.getPubsubMessage().getMessageId())
                .body(message.getPubsubMessage().getData().toString())
                .ackStatus(PubSubMessageStatus.OUTSTANDING)
                .receiptCount(1)
                .build();
    }

    private static RmsMessage buildRmsMessage(BasicAcknowledgeablePubsubMessage message) {
        return RmsMessage.builder()
                .orderingKey(message.getPubsubMessage().getOrderingKey())
                .attributesMap(message.getPubsubMessage().getAttributesMap())
                .gcpPubSubMessageId(message.getPubsubMessage().getMessageId())
                .tradeIdVersion(buildTradeIdVersionObject(message))
                .body(message.getPubsubMessage().getData().toString())
                .receiptCount(1)
                .ackStatus(PubSubMessageStatus.OUTSTANDING)
                .build();
    }
}
