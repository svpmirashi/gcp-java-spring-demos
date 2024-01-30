package gcp.demo.pubsub.service;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import gcp.demo.pubsub.model.*;
import gcp.demo.pubsub.repository.PubSubMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PubSubMessageService {
//    private static Map<TradeIdVersion, RmsMessage> rmsMessageCache = new HashMap<>();
//    private static List<PlainMessage> plainMessageCache = new ArrayList<>();
    @Autowired
    private PubSubMessageRepository pubSubMessageRepository;

    public PubSubMessage save(BasicAcknowledgeablePubsubMessage message) {
        return pubSubMessageRepository.save(message);
    }

    public PubSubMessage getPubSubMessage(TradeIdVersion key, String messageId) {
        return pubSubMessageRepository.getById(key, messageId);
    }

    public RmsMessage getRmsMessage(TradeIdVersion key) {
        return (RmsMessage) pubSubMessageRepository.getById(key);
    }
    public PlainMessage getPlainMessage(String messageId) {
        return (PlainMessage) pubSubMessageRepository.getById(null, messageId);
    }
}
