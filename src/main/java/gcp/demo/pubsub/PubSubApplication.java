package gcp.demo.pubsub;

import java.io.IOException;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import gcp.demo.pubsub.model.PubSubMessage;
import gcp.demo.pubsub.model.RmsMessage;
import gcp.demo.pubsub.service.PubSubMessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

@SpringBootApplication
public class PubSubApplication {

  private static final Log LOGGER = LogFactory.getLog(PubSubApplication.class);

  public static void main(String[] args) throws IOException {
    SpringApplication.run(PubSubApplication.class, args);
  }

  @Autowired
  private PubSubConfiguration pubSubConfiguration;

  @Autowired
  private PubSubMessageService pubSubMessageService;

  // Create a message channel for messages arriving from the subscription `sub-one`.
  @Bean
  @Qualifier("pubsubInputMessageChannel")
  public MessageChannel pubsubInputMessageChannel() {
    return new PublishSubscribeChannel();
    //return new DirectChannel();
  }

  // Create an inbound channel adapter to listen to the subscription `sub-one` and send
  // messages to the input message channel.
  @Bean
  public PubSubInboundChannelAdapter inboundChannelAdapter(
      @Qualifier("pubsubInputMessageChannel") MessageChannel messageChannel,
      PubSubTemplate pubSubTemplate) {
    String subscriptionName = pubSubConfiguration.getSubscriptionName(); // "rms-trade-poc-topic-1-ordered-delivery-sub"; // sub-one

    LOGGER.info("Application Name: " + pubSubConfiguration.getConsumerName());
    LOGGER.info("Subscription: " + subscriptionName);

    PubSubInboundChannelAdapter adapter =
        new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
    adapter.setOutputChannel(messageChannel);
    adapter.setAckMode(AckMode.MANUAL);
    adapter.setPayloadType(String.class);
    return adapter;
  }

  // Define what happens to the messages arriving in the message channel.
  @ServiceActivator(inputChannel = "pubsubInputMessageChannel")
  public void messageReceiver(
      String payload,
      @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
    PubSubMessage savedMessage = pubSubMessageService.save(message);
    //logPubsubMessage(payload, message);
    if(savedMessage instanceof RmsMessage) {
      ((RmsMessage) savedMessage).display();
    }
    else {
      savedMessage.display();
    }
    message.ack();
    // Update ACK status of the message
  }

  private void logPubsubMessage(String payload, BasicAcknowledgeablePubsubMessage message) {
    StringBuilder log = new StringBuilder("");
    log.append("{");
    log.append(pubSubConfiguration.getConsumerName());
    log.append(": Message_ID: ");
    log.append(message.getPubsubMessage().getOrderingKey().isEmpty()? "None" : message.getPubsubMessage().getMessageId());
    log.append(": Ordering_Key: ");
    log.append(message.getPubsubMessage().getOrderingKey().isEmpty()? "None" : message.getPubsubMessage().getOrderingKey());
    log.append(", Attributes: ");
    log.append(message.getPubsubMessage().getAttributesMap());
    log.append(", Payload: ");
    log.append(payload);
    log.append("}");

    LOGGER.info("Message Received: " + log.toString());
  }
}
